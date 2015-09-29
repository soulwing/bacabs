package io.mikesir87.bacabs.service.docker.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Code from Arquillian Cube project, found at:
 * https://github.com/arquillian/arquillian-cube
 */
public abstract class AbstractCliInternetAddressResolver {

  public static final String DOCKERHOST_TAG = "dockerHost";
  private static final Logger log = LoggerFactory.getLogger(AbstractCliInternetAddressResolver.class);

  private CommandLineExecutor commandLineExecutor;
  private String cachedIp = null;

  public AbstractCliInternetAddressResolver(final CommandLineExecutor commandLineExecutor) {
    this.commandLineExecutor = commandLineExecutor;
  }

  public String ip(boolean force) {
    if (cachedIp == null || force) {
      cachedIp = getIp();
    }
    return cachedIp;
  }

  private String getIp() {
    String output = commandLineExecutor.execCommand(getCommandArguments());
    Matcher m = getIpPattern().matcher(output);
    if (m.find()) {
      String ip = m.group();
      return ip;
    } else {
      String errorMessage = String.format("Cli Internet address resolver executed %s command and does not return a valid util. It returned %s.",
          Arrays.toString(getCommandArguments()), output);
      log.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
  }

  protected abstract String[] getCommandArguments();

  protected abstract Pattern getIpPattern();
}
