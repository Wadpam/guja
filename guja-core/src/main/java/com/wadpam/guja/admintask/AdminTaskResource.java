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

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.google.inject.servlet.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Start admin tasks.
 * Admin tasks a run as separate GAE tasks and have a 10 minute execution time.
 *
 * @author osandstrom
 * @author mattiaslevin
 */
@Path("_adm/task")
@Singleton
@PermitAll // Protected by container security
public class AdminTaskResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminTaskResource.class);

  private final Set<AdminTask> adminTasks;
  private final AdminTaskQueue taskQueue;

  @Inject @RequestParameters
  private Provider<Map<String, String[]>> requestParamsProvider;

  @Inject
  public AdminTaskResource(Set<AdminTask> adminTasks, AdminTaskQueue taskQueue) {
    this.adminTasks = adminTasks;
    this.taskQueue = taskQueue;
  }

  /**
   * Enqueue an admin task for processing.
   * The request will return immediately and the task will be run in a separate thread.
   * The execution model depending on the implementation of the queue.
   *
   * @param taskName task name
   * @return
   */
  @GET
  @Path("{taskName}")
  public Response enqueueTask(@PathParam("taskName") String taskName) {
    checkNotNull(taskName);
    taskQueue.enqueueTask(taskName, requestParamsProvider.get());
    return Response.ok().build();
  }


  /**
   * Process an admin task.
   * The admin task will be processed on the requesting thread.
   *
   * @param taskName task name
   * @return
   */
  @POST
  @Path("{taskName}")
  public Response processTask(@PathParam("taskName") String taskName) {
    checkNotNull(requestParamsProvider.get());
    checkNotNull(taskName);

    LOGGER.info("Processing task for {}...", taskName);
    for (AdminTask adminTask : adminTasks) {
      final Object body = adminTask.processTask(taskName, requestParamsProvider.get());
      LOGGER.info("Processed tasks for {}: {}", taskName, body);
    }

    return Response.ok().build();
  }

}
