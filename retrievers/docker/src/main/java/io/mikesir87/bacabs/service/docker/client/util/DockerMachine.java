package io.mikesir87.bacabs.service.docker.client.util;

import java.util.regex.Pattern;

/**
 * Code from Arquillian Cube project, licensed under ASL 2.0. Can be found at:
 * https://github.com/arquillian/arquillian-cube
 *
 * https://github.com/arquillian/arquillian-cube/blob/master/license-asl-2.0.txt
 *
 * Modified to specify a machine name at construction, rather than setting
 * later.
 */
public class DockerMachine extends AbstractCliInternetAddressResolver {

  private static final String DOCKER_MACHINE_EXEC = "docker-machine";

  private static final Pattern IP_PATTERN = Pattern.compile("(?:\\d{1,3}\\.){3}\\d{1,3}");

  private final String machineName;

  public DockerMachine(CommandLineExecutor commandLineExecutor, String machineName) {
    super(commandLineExecutor);
    this.machineName = machineName;
  }

  @Override
  protected String[] getCommandArguments() {
    if (machineName == null) {
      throw new IllegalArgumentException("Machine Name cannot be null");
    }

    return new String[]{createDockerMachineCommand() ,"ip", machineName};
  }

  @Override
  protected Pattern getIpPattern() {
    return IP_PATTERN;
  }

  private String createDockerMachineCommand() {
    return DOCKER_MACHINE_EXEC;
  }
}