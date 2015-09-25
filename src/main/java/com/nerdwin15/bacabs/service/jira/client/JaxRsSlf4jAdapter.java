package com.nerdwin15.bacabs.service.jira.client;

import org.slf4j.Logger;

/**
 * A logger adapter for SLF4j.
 *
 * @author Carl Harris
 */
public class JaxRsSlf4jAdapter implements JaxRsLoggerAdapter {

  private final Logger logger;

  /**
   * Constructs a new adapter instance that delegates to the given logger.
   * @param logger logger logger
   */
  public JaxRsSlf4jAdapter(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void log(int level, String format, Object... args) {
    switch (level) {
      case INFO:
        logger.info(String.format(format, args));
        break;
      case DEBUG:
        if (logger.isDebugEnabled()) {
          logger.debug(String.format(format, args));
        }
        break;
      default:
      case TRACE:
        if (logger.isTraceEnabled()) {
          logger.trace(String.format(format, args));
        }
        break;
    }
  }

}