package com.wadpam.guja.admintask;

/*
 * #%L
 * guja-base
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Start admin tasks.
 * Admin tasks a run as separate GAE tasks and have a 10 minute execution time.
 *
 * @author osandstrom
 * @author mattiaslevin
 */
@Path("adm/task")
@Singleton
@PermitAll // Protected by container security
public class AdminTaskResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminTaskResource.class);

  public static final String PATH_ADMIN_TASK = "/adm/task/%s";

  private final Set<AdminTask> adminTasks;

  @Inject
  public AdminTaskResource(Set<AdminTask> adminTasks) {
    this.adminTasks = adminTasks;
  }

  @GET
  @Path("{taskName}")
  public Response enqueueTask(@RequestParameters Map<String, String[]> paramMap,
                              @PathParam("taskName") String taskName) {
    checkNotNull(taskName);

    final Queue queue = QueueFactory.getDefaultQueue();
    final TaskOptions options = TaskOptions.Builder.withUrl(String.format(PATH_ADMIN_TASK, taskName));
    for (Entry<String, String[]> param : paramMap.entrySet()) {
      for (String value : param.getValue()) {
        options.param(param.getKey(), value);
      }
    }

    try {
      queue.add(options);
    } catch (TransientFailureException tfe) {
      LOGGER.error("Run admin task fail {} ", tfe.getMessage());
    }

    LOGGER.info("Added admin task to queue {}", taskName);

    return Response.ok().build();
  }


  @POST
  @Path("{taskName}")
  public Response processTask(@RequestParameters Map<String, String[]> paramMap,
                              @PathParam("taskName") String taskName) {
    checkNotNull(paramMap);
    checkNotNull(taskName);

    LOGGER.info("Processing task for {}...", taskName);
    for (AdminTask adminTask : adminTasks) {
      final Object body = adminTask.processTask(taskName, paramMap);
      LOGGER.info("Processed tasks for {}: {}", taskName, body);
    }

    return Response.ok().build();
  }

}

