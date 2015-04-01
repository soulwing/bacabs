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
package com.nerdwin15.bacabs.service;

import static org.jboss.as.controller.client.helpers.ClientConstants.DEPLOYMENT;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP_ADDR;
import static org.jboss.as.controller.client.helpers.ClientConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.client.helpers.ClientConstants.RESULT;
import static org.jboss.as.controller.client.helpers.ClientConstants.SUBSYSTEM;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import com.nerdwin15.bacabs.ConcreteDeployment;
import com.nerdwin15.bacabs.ConcreteGitBranch;
import com.nerdwin15.bacabs.ConcreteJiraIssue;
import com.nerdwin15.bacabs.Deployment;
import com.nerdwin15.bacabs.service.git.GitBranchRetrievalService;
import com.nerdwin15.bacabs.service.jira.JiraIssueRetrievalService;
import com.nerdwin15.bacabs.service.jira.JiraLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a service that is used to sync the deployments by asking the JBoss
 * management.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class WildflyRemoteDeploymentRetriever implements RemoteDeploymentRetriever {

  private static final Logger logger = LoggerFactory.getLogger(WildflyRemoteDeploymentRetriever.class);

  @Inject @org.soulwing.cdi.properties.Property
  protected String jbossUsername;
  
  @Inject @org.soulwing.cdi.properties.Property
  protected String jbossPassword;
  
  @Inject @org.soulwing.cdi.properties.Property
  protected Integer jbossPort;
  
  @Inject @org.soulwing.cdi.properties.Property
  protected String hrefRoot;

  @Inject @org.soulwing.cdi.properties.Property
  protected String jiraIdPattern;
  
  @Inject
  private JiraLoginService casLoginService;
  
  @Inject 
  JiraIssueRetrievalService jiraIssueRetrievalService;

  @Inject
  GitBranchRetrievalService gitBranchRetrievalService;
  
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
      logger.info("Have href: " + href);
      if (href == null || href.isEmpty())
        continue;

      String identifier = href.substring(1);

      ConcreteJiraIssue issue = null;
      ConcreteGitBranch branch = null;
      if (identifier != null
          && !identifier.isEmpty()
          && identifier.contains(jiraIdPattern)) {
        issue = getJiraIssue(identifier);
        branch = getGitlabBranch(identifier);
      }

      ConcreteDeployment deployment = new ConcreteDeployment();
      deployment.setHref(hrefRoot + href);
      deployment.setIdentifier(identifier);
      deployment.setJiraIssue(issue);
      deployment.setGitlabBranch(branch);
      deployments.add(deployment);
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

  private ConcreteJiraIssue getJiraIssue(String identifier) {
    try {
      String url = casLoginService.getAuthenticatedRestUrl(identifier);
      return (ConcreteJiraIssue) jiraIssueRetrievalService.retrieveJiraIssue(url);
    }
    catch (IOException e) {
      System.out.println("Unable to retrieve Jira Issue.  IO problem occurred.");
      return null;
    }
  }
  
  private ConcreteGitBranch getGitlabBranch(String identifier) {
    return (ConcreteGitBranch) gitBranchRetrievalService.retrieveGitBranch(identifier);
  }
}
