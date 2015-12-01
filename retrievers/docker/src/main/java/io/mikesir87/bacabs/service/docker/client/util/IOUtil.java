package io.mikesir87.bacabs.service.docker.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Code from Arquillian Cube project, licensed under ASL 2.0. Can be found at:
 * https://github.com/arquillian/arquillian-cube
 *
 * https://github.com/arquillian/arquillian-cube/blob/master/license-asl-2.0.txt
 *
 * Removed many of the other helpers and only kept needed methods.
 */
public class IOUtil {

  private IOUtil() {
    super();
  }

  public static final String asString(InputStream response) {

    StringWriter logwriter = new StringWriter();

    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response))) {

      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        logwriter.write(line);
      }

      return logwriter.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}