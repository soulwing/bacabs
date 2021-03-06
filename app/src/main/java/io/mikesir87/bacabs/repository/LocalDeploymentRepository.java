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
package io.mikesir87.bacabs.repository;

import io.mikesir87.bacabs.Deployment;
import io.mikesir87.bacabs.domain.ConcreteDeployment;
import org.apache.commons.lang.Validate;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * An in-memory implementation of the {@link DeploymentRepository}
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class LocalDeploymentRepository implements DeploymentRepository {

  private Set<ConcreteDeployment> deployments = new HashSet<>();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<? extends Deployment> getDeployments() {
    return deployments;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addDeployment(Deployment deployment) {
    Validate.isTrue(deployment instanceof ConcreteDeployment);
    deployments.add((ConcreteDeployment) deployment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Deployment updateDeployment(Deployment deployment,
      DeploymentChangeListener changeListener) {
    Deployment existing = findDeployment(deployment);
    if (existing == null) {
      addDeployment(deployment);
      return deployment;
    }
    ConcreteDeployment e = (ConcreteDeployment) existing;
    ConcreteDeployment d = (ConcreteDeployment) deployment;

    e.setGitBranch(d.getGitBranch());
    e.setHref(d.getHref());
    e.setIdentifier(d.getIdentifier());
    e.setJiraIssue(d.getJiraIssue());
    e.setSummary(d.getSummary());

    if (e.getStatus() != d.getStatus()) {
      changeListener.statusChanged(e.getStatus(), d.getStatus());
    }

    if (e.getStatus().equals(Deployment.Status.UNKNOWN) &&
        d.getStatus().equals(Deployment.Status.VERIFIED)) {
      e.setDiscoveryTime(new Date());
    }
    e.setStatus(d.getStatus());



    return e;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeDeployment(Deployment deployment) {
    deployments.remove(deployment);
  }

  private Deployment findDeployment(Deployment deployment) {
    for (Deployment d : deployments) {
      if (d.equals(deployment))
        return d;
    }
    return null;
  }
}
