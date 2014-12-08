/*
 * File created on Dec 6, 2014 
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
package com.nerdwin15.bacabs.service;

import java.io.IOException;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.soulwing.cdi.properties.Property;

import com.nerdwin15.bacabs.Deployment;

/**
 * Straight-forward implementation of the {@link DeploymentSyncService}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DeploymentSyncServiceBean implements DeploymentSyncService {

  @Inject
  protected RemoteDeploymentRetriever deploymentRetriever;
  
  @Inject
  protected DeploymentService deploymentService;
  
  @Inject @Property
  protected String identifierPattern;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void performDeploymentSync() throws IOException {
    Set<? extends Deployment> knownDeployments = deploymentService.getDeployments();
    
    for (Deployment deployment : deploymentRetriever.fetchDeployments()) {
      if (knownDeployments.contains(deployment)) {
        knownDeployments.remove(deployment);
        continue;
      }
      
      if (!deployment.getIdentifier().matches(identifierPattern))
        continue;
      
      deploymentService.addDeployment(deployment);
      System.out.println("Discovered new deployment: " + deployment);
    }
    
    for (Deployment deployment : knownDeployments) {
      System.out.println("Removing old deployment: " + deployment);
      deploymentService.removeDeployment(deployment);
    }
  }

}
