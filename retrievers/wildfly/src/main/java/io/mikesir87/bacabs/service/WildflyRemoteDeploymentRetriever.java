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
import io.mikesir87.bacabs.DeploymentBuilder;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.as.controller.client.helpers.ClientConstants.DEPLOYMENT;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP_ADDR;
import static org.jboss.as.controller.client.helpers.ClientConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.client.helpers.ClientConstants.RESULT;
import static org.jboss.as.controller.client.helpers.ClientConstants.SUBSYSTEM;

/**
 * Defines a service that is used to sync the deployments by asking the JBoss
 * management.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class WildflyRemoteDeploymentRetriever implements RemoteDeploymentRetriever {

  @Inject
  @org.soulwing.cdi.properties.Property(name = "wildfly.adminUsername")
  protected String jbossUsername;

  @Inject
  @org.soulwing.cdi.properties.Property(name = "wildfly.adminPassword")
  protected String jbossPassword;

  @Inject
  @org.soulwing.cdi.properties.Property(name = "wildfly.adminHost")
  protected String jbossAdminHost;

  @Inject
  @org.soulwing.cdi.properties.Property(name = "wildfly.adminPort")
  protected Integer jbossPort;

  @Inject
  @org.soulwing.cdi.properties.Property(name = "wildfly.deploymentBaseUrl")
  protected String hrefRoot;

  @Inject
  protected DeploymentBuilder deploymentBuilder;

  protected ModelControllerClient wildflyClient;

  @PostConstruct
  public void postConstruct() {
    try {
      wildflyClient = ModelControllerClient.Factory.create(
          InetAddress.getByName("127.0.0.1"), jbossPort);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Set<? extends Deployment> fetchDeployments() throws IOException {
    Set<Deployment> deployments = new HashSet<>();

    final ModelNode request = new ModelNode();
    request.get(OP).set(READ_RESOURCE_OPERATION);
    request.get(OP_ADDR).add(DEPLOYMENT);

    ModelNode returnVal = wildflyClient.execute(request);

    for (Property property : returnVal.get(RESULT).get(DEPLOYMENT).asPropertyList()) {
      String href = getHref(property.getName());
      if (href == null || href.isEmpty())
        continue;

      String identifier = href.substring(1);
      href = hrefRoot + href;
      deployments.add(deploymentBuilder.href(href).identifier(identifier).build());
    }


    return deployments;
  }

  private String getHref(String deploymentName) throws IOException {
    final ModelNode request = new ModelNode();
    request.get(OP).set(READ_RESOURCE_OPERATION);
    request.get(OP_ADDR)
        .add(DEPLOYMENT).add(deploymentName).add(SUBSYSTEM).add("undertow");
    ModelNode returnVal = wildflyClient.execute(request);
    if (returnVal.isDefined() && returnVal.get(RESULT).isDefined())
      return returnVal.get(RESULT).get("context-root").asString();
    return null;
  }

}
