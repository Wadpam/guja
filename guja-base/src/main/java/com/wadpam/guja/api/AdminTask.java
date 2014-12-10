package com.wadpam.guja.api;

import java.util.Map;

/**
 * Process and run admin tasks.
 *
 * @author osandstrom
 */
public interface AdminTask {
  Object processTask(String taskName, Map<String, String[]> parameterMap);
}


