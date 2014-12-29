package com.wadpam.guja.admintask;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default admin task queue implementation.
 * The admin task will run the task in a fixed sized thread pool.
 * @author mattiaslevin
 */
@Singleton
public class DefaultAdminTaskQueue implements AdminTaskQueue {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdminTaskQueue.class);

  private static final int MAX_PARALLEL_ADMIN_TASKS = 2;

  private final ExecutorService excutor = Executors.newFixedThreadPool(MAX_PARALLEL_ADMIN_TASKS);

  private final Set<AdminTask> adminTasks;

  @Inject
  public DefaultAdminTaskQueue(Set<AdminTask> adminTasks) {
    this.adminTasks = adminTasks;
  }

  @Override
  public void enqueueTask(final String taskName, final Map<String, String[]> paramMap) {

    excutor.execute(new Runnable() {
      @Override
      public void run() {
        for (AdminTask adminTask : adminTasks) {
          final Object body = adminTask.processTask(taskName, paramMap);
          LOGGER.info("Processed tasks for {}: {}", taskName, body);
        }
      }
    });

    LOGGER.info("Added admin task to queue {}", taskName);

  }

}
