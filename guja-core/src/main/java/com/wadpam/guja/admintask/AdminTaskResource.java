package com.wadpam.guja.admintask;

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

    final Queue queue = QueueFactory.getDefaultQueue();
    final TaskOptions options = TaskOptions.Builder.withUrl(String.format(PATH_ADMIN_TASK, checkNotNull(taskName)));
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
    checkNotNull(taskName);
    checkNotNull(paramMap);

    LOGGER.info("Processing task for {}...", taskName);
    for (AdminTask adminTask : adminTasks) {
      final Object body = adminTask.processTask(taskName, paramMap);
      LOGGER.info("Processed tasks for {}: {}", taskName, body);
    }

    return Response.ok().build();
  }

}

