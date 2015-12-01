package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.Deployment;

/**
 * A service that can be used to check if a deployment is actually up or not.
 *
 * @author Michael Irwin
 */
public interface StatusCheckingService {

  /**
   * Get the status for the provided deployment
   * @param deployment The deployment to check status for.
   * @return The deployment's status
   */
  Deployment.Status getStatus(Deployment deployment);
}
