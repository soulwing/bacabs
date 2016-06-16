package io.mikesir87.bacabs.service.docker;

/**
 * An interface that provides details about a running Docker container.
 *
 * @author Michael Irwin
 */
public interface DockerContainer {

  /**
   * Get public href for the container (most likely the virtual host name)
   * @return The public href
   */
  String getHref();

  /**
   * Get the deployment identifier for the container
   * @return The deployment identifier for the container
   */
  String getIdentifier();
}
