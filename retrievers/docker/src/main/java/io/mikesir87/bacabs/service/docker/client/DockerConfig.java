package io.mikesir87.bacabs.service.docker.client;

/**
 * A configuration object that provides details about the Docker
 * environment.
 *
 * @author Michael Irwin
 */
public interface DockerConfig {

  /**
   * Is Docker running locally, and not through a docker machine?
   * @return True if running locally. Otherwise, true.
   */
  boolean isRunningLocally();

  /**
   * Get the port that the Docker server is running on, if it
   * is running locally.
   * @return The local port Docker is listening on
   */
  Integer getLocalPort();

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
