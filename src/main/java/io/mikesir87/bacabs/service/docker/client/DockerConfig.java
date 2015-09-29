package io.mikesir87.bacabs.service.docker.client;

/**
 * A configuration object that provides details about the Docker
 * environment.
 *
 * @author Michael Irwin
 */
public interface DockerConfig {

  /**
   * Get the IP address for the Docker server
   * @return IP address for the Docker server
   */
  String getIp();

  /**
   * Get the cert path for the Docker server
   * @return The cert path
   */
  String getDockerCertPath();

  /**
   * Get the name of the Docker machine being used
   * @return The name of the Docker machine
   */
  String getDockerMachine();
}
