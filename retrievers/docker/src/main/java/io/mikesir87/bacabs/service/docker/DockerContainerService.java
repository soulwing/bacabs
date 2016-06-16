package io.mikesir87.bacabs.service.docker;

import java.util.Set;

/**
 * A service that provides information about running Docker containers.
 *
 * @author Michael Irwin
 */
public interface DockerContainerService {

  /**
   * Get all Docker containers that are utilizing the nginx-proxy
   * @return The running containers
   */
  Set<DockerContainer> getHostedContainers();
}
