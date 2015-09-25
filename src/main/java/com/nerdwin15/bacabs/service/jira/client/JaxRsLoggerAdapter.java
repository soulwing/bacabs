package com.nerdwin15.bacabs.service.jira.client;

/**
 * An adapter for a logger used by {@link JaxRsLoggingFilter}.
 *
 * @author Carl Harris
 */
public interface JaxRsLoggerAdapter {

  int TRACE = 0;
  int DEBUG = 1;
  int INFO = 2;

  void log(int level, String format, Object... args);

}
