package io.mikesir87.bacabs.service.docker.client.util;

import java.io.IOException;

/**
 * Code from Arquillian Cube project, licensed under ASL 2.0. Can be found at:
 * https://github.com/arquillian/arquillian-cube
 *
 * https://github.com/arquillian/arquillian-cube/blob/master/license-asl-2.0.txt
 */
public class CommandLineExecutor {

  public String execCommand(String... arguments) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(arguments);
      processBuilder.redirectErrorStream(true);
      Process pwd = processBuilder.start();

      pwd.waitFor();
      String output = IOUtil.asString(pwd.getInputStream());
      return output;
    } catch (InterruptedException | IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
