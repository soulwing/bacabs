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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.cdi.properties.Property;

import com.nerdwin15.bacabs.Deployment;

/**
 * Straight-forward implementation of the {@link DeploymentSyncService}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DeploymentSyncServiceBean implements DeploymentSyncService {

  private static final Logger logger = LoggerFactory.getLogger(DeploymentSyncServiceBean.class);

  @Inject
  protected RemoteDeploymentRetriever deploymentRetriever;

  @Inject
  protected DeploymentService deploymentService;

  @Inject @Property
  protected String identifierPattern;

  @PostConstruct
  public void init() {
    logger.info("Looking for deployments with pattern: " + identifierPattern);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void performDeploymentSync() throws IOException {
    Set<? extends Deployment> knownDeployments = deploymentService.getDeployments();

    for (Deployment deployment : deploymentRetriever.fetchDeployments()) {
      if (knownDeployments.contains(deployment)) {
        knownDeployments.remove(deployment);
        deploymentService.addDeployment(deployment);
        continue;
      }

      if (!deployment.getIdentifier().matches(identifierPattern))
        continue;

      deploymentService.addDeployment(deployment);
      logger.info("Discovered new deployment: " + deployment);
    }

    for (Deployment deployment : knownDeployments) {
      logger.info("Removing old deployment: " + deployment);
      deploymentService.removeDeployment(deployment);
    }
  }

}
