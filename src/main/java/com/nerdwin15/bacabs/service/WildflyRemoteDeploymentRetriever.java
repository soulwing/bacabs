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
import com.nerdwin15.bacabs.ConcreteGitlabBranch;
import com.nerdwin15.bacabs.ConcreteJiraIssue;
import com.nerdwin15.bacabs.Deployment;
import com.nerdwin15.bacabs.service.gitlab.GitlabBranchRetrievalService;
import com.nerdwin15.bacabs.service.jira.JiraIssueRetrievalService;
import com.nerdwin15.bacabs.service.jira.JiraLoginService;

/**
 * Defines a service that is used to sync the deployments by asking the JBoss
 * management.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class WildflyRemoteDeploymentRetriever implements RemoteDeploymentRetriever {

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
  GitlabBranchRetrievalService gitlabBranchRetrievalService;
  
  protected ModelControllerClient wildflyClient;
  
  @PostConstruct
  public void postConstruct() throws Exception {
    wildflyClient = ModelControllerClient.Factory.create(
        InetAddress.getByName("127.0.0.1"), jbossPort);
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
     
      ConcreteJiraIssue issue = null;
      ConcreteGitlabBranch branch = null;
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

    /*
    // Mocked data for testing
    ConcreteDeployment deployment = new ConcreteDeployment();
    deployment.setHref(hrefRoot + "/CREST-1033");
    deployment.setIdentifier("CREST-1033");
    deployment.setJiraIssue(getJiraIssue("CREST-1033"));
    deployment.setGitlabBranch(getGitlabBranch("CREST-1033"));
    deployments.add(deployment);
    
    ConcreteDeployment deployment2 = new ConcreteDeployment();
    deployment2.setHref(hrefRoot + "/CREST-965");
    deployment2.setIdentifier("CREST-965");
    deployment2.setJiraIssue(getJiraIssue("CREST-965"));
    deployment2.setGitlabBranch(getGitlabBranch("CREST-965"));
    deployments.add(deployment2);
    
    ConcreteDeployment deployment3 = new ConcreteDeployment();
    deployment3.setHref(hrefRoot + "/CREST-1039");
    deployment3.setIdentifier("CREST-1039");
    deployment3.setJiraIssue(getJiraIssue("CREST-1039"));
    deployment3.setGitlabBranch(getGitlabBranch("CREST-1039"));
    deployments.add(deployment3);
    
    ConcreteDeployment deployment4 = new ConcreteDeployment();
    deployment4.setHref(hrefRoot + "/CREST-131");
    deployment4.setIdentifier("CREST-131");
    deployment4.setJiraIssue(getJiraIssue("CREST-131"));
    deployment4.setGitlabBranch(getGitlabBranch("CREST-131"));
    deployments.add(deployment4);
    
    ConcreteDeployment deployment5 = new ConcreteDeployment();
    deployment5.setHref(hrefRoot + "/CREST-1161");
    deployment5.setIdentifier("CREST-1161");
    deployment5.setJiraIssue(getJiraIssue("CREST-1161"));
    deployment5.setGitlabBranch(getGitlabBranch("CREST-1161"));
    deployments.add(deployment5);
    // End - mocked data 
    */
    
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
  
  private ConcreteGitlabBranch getGitlabBranch(String identifier) {
    return (ConcreteGitlabBranch) gitlabBranchRetrievalService.retrieveGitlabBranch(identifier); 
  }
}
