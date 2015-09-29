package io.mikesir87.bacabs.service.docker;

/**
 * An interface that provides details about a Wildfly container running in a
 * Docker container.
 *
 * @author Michael Irwin
 */
public interface DockerWildflyContainer {

  /**
   * Get the href for the main HTTP listener
   * @return The href for the main HTTP listener
   */
  String getHref();

  /**
   * Get the deployment identifier for the container
   * @return The deployment identifier for the container
   */
  String getIdentifier();
}
