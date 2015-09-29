package io.mikesir87.bacabs.service.docker.client.util;

/**
 * Code from Arquillian Cube project, licensed under ASL 2.0. Can be found at:
 * https://github.com/arquillian/arquillian-cube
 *
 * https://github.com/arquillian/arquillian-cube/blob/master/license-asl-2.0.txt
 */
public class HomeResolverUtil {

  public static String resolveHomeDirectoryChar(String path) {
    if (path.startsWith("~")) {
      return path.replace("~", System.getProperty("user.home"));
    }
    return path;
  }
}