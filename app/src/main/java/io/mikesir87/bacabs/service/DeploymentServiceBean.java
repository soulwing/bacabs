/*
 * File created on Dec 5, 2014 
 *
 * Copyright (c) 2014 Nerdwin15, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.Deployment;
import io.mikesir87.bacabs.event.DeploymentStatusChangeEvent;
import io.mikesir87.bacabs.event.NewDeploymentEvent;
import io.mikesir87.bacabs.event.RemovedDeploymentEvent;
import io.mikesir87.bacabs.event.UpdatedDeploymentEvent;
import io.mikesir87.bacabs.repository.DeploymentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * A CDI-injectable implementation of the {@link DeploymentService}
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DeploymentServiceBean implements DeploymentService {

  @Inject
  protected DeploymentRepository deploymentRepository;
  
  @Inject
  protected Event<NewDeploymentEvent> newDeploymentEvent;

  @Inject
  protected Event<RemovedDeploymentEvent> removedDeploymentEvent;

  @Inject
  protected Event<UpdatedDeploymentEvent> updatedDeploymentEvent;

  @Inject
  protected Event<DeploymentStatusChangeEvent> statusChangeEvent;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<? extends Deployment> getDeployments() {
    return new HashSet<Deployment>(deploymentRepository.getDeployments());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addDeployment(Deployment deployment) {
    deploymentRepository.addDeployment(deployment);
    newDeploymentEvent.fire(new NewDeploymentEvent(deployment));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateDeployment(Deployment deployment) {
    deployment = deploymentRepository.updateDeployment(deployment,
        new ChangeListener(deployment));
    updatedDeploymentEvent.fire(new UpdatedDeploymentEvent(deployment));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeDeployment(Deployment deployment) {
    deploymentRepository.removeDeployment(deployment);
    removedDeploymentEvent.fire(new RemovedDeploymentEvent(deployment));
  }


  private class ChangeListener implements DeploymentRepository.DeploymentChangeListener {

    private final Deployment deployment;

    public ChangeListener(Deployment deployment) {
      this.deployment = deployment;
    }

    @Override
    public void statusChanged(Deployment.Status oldStatus, Deployment.Status newStatus) {
      statusChangeEvent.fire(new DeploymentStatusChangeEvent(deployment, oldStatus));
    }
  }
}
