package io.mikesir87.bacabs.event;

import io.mikesir87.bacabs.Deployment;

/**
 * Created by mikesir on 6/15/16.
 */
public class DeploymentStatusChangeEvent extends AbstractDeploymentEvent {

  private static final String TYPE = DeploymentStatusChangeEvent.class.getSimpleName();

  private final Deployment.Status oldStatus;

  public DeploymentStatusChangeEvent(Deployment deployment,
      Deployment.Status oldStatus) {
    super(deployment);
    this.oldStatus = oldStatus;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  public Deployment.Status getOldStatus() {
    return oldStatus;
  }
}
