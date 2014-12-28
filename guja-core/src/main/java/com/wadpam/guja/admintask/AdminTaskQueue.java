package com.wadpam.guja.admintask;

import java.util.Map;

/**
 * Admin task queue.
 *
 * @author mattiaslevin
 */
public interface AdminTaskQueue {

  void enqueueTask(String taskName, Map<String, String[]> paramMap);

}
