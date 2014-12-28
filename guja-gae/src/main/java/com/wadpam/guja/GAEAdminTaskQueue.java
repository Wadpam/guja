package com.wadpam.guja;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.wadpam.guja.admintask.AdminTaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * GAE implementation of admin task queue based on task queues.
 *
 * @author mattiaslevin
 */
public class GAEAdminTaskQueue implements AdminTaskQueue {
  private static final Logger LOGGER = LoggerFactory.getLogger(GAEAdminTaskQueue.class);

  public static final String PATH_ADMIN_TASK = "/adm/task/%s";


  @Override
  public void enqueueTask(String taskName, Map<String, String[]> paramMap) {

    checkNotNull(taskName);

    final Queue queue = QueueFactory.getDefaultQueue();
    final TaskOptions options = TaskOptions.Builder.withUrl(String.format(PATH_ADMIN_TASK, taskName));

    for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
      for (String value : param.getValue()) {
        options.param(param.getKey(), value);
      }
    }

    try {
      queue.add(options);
      LOGGER.info("Added admin task to queue {}", taskName);
    } catch (TransientFailureException tfe) {
      LOGGER.error("Run admin task fail {} ", tfe.getMessage());
    }

  }

}
