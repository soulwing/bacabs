/*
 * File created on Dec 8, 2014 
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

import com.nerdwin15.bacabs.Deployment;
import com.nerdwin15.bacabs.GitBranch;
import com.nerdwin15.bacabs.JiraIssue;
import com.nerdwin15.bacabs.service.git.GitBranchRetrievalService;
import com.nerdwin15.bacabs.service.jira.JiraIssueRetriever;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test case for the {@link DeploymentSyncServiceBean}.
 *
 * @author Michael Irwin
 */
public class DeploymentSyncServiceBeanTest {

  private static final String KNOWN_ID_1 = "accept1";
  private static final String KNOWN_ID_2 = "accept2";
  private static final String NEW_ID_1 = "accept-new-1";
  private static final String BAD_NEW_ID = "reject-new-1";
  private static final String MATCH_PATTERN = "accept.*";
  
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock DeploymentService deploymentService;
  @Mock RemoteDeploymentRetriever deploymentRetriever;
  @Mock JiraIssueRetriever jiraIssueRetriever;
  @Mock GitBranchRetrievalService gitBranchRetrievalService;

  @Mock JiraIssue jiraIssue1;
  @Mock JiraIssue jiraIssue2;
  @Mock JiraIssue jiraIssue3;
  @Mock GitBranch gitBranch1;
  @Mock GitBranch gitBranch2;
  @Mock GitBranch gitBranch3;

  @Mock Deployment knownDeployment1;
  @Mock Deployment knownDeployment2;
  @Mock Deployment newDeployment;
  @Mock Deployment badNewDeployment;
  
  private Set<Deployment> discoveredDeployments = new HashSet<>();
  private Set<Deployment> knownDeployments = new HashSet<>();
  
  private DeploymentSyncServiceBean service = new DeploymentSyncServiceBean();
  
  /**
   * Perform setup tasks
   */
  @Before
  public void setUp() throws Exception {
    service.deploymentRetriever = deploymentRetriever;
    service.deploymentService = deploymentService;
    service.identifierPattern = MATCH_PATTERN;
    service.jiraIssueRetriever = jiraIssueRetriever;
    service.gitBranchRetrievalService = gitBranchRetrievalService;

    setDeploymentExpectations();
    knownDeployments.add(knownDeployment1);
    knownDeployments.add(knownDeployment2);
    
    discoveredDeployments.add(knownDeployment2);
    discoveredDeployments.add(newDeployment);
    discoveredDeployments.add(badNewDeployment);
  }
  
  @Test
  public void testSync() throws Exception {
    setJiraIssueRetrieverExpectations();
    setGitBranchRetrieverExpectations();

    context.checking(new Expectations() {
      {
        oneOf(deploymentService).getDeployments();
        will(returnValue(knownDeployments));
        oneOf(deploymentRetriever).fetchDeployments();
        will(returnValue(discoveredDeployments));
        oneOf(deploymentService).updateDeployment(knownDeployment2);
        oneOf(deploymentService).addDeployment(newDeployment);
        oneOf(deploymentService).removeDeployment(knownDeployment1);
      }
    });
    
    service.performDeploymentSync();
  }
  
  
  private void setDeploymentExpectations() {
    context.checking(new Expectations() { {
      oneOf(knownDeployment2).getIdentifier();
      will(returnValue(KNOWN_ID_2));
      oneOf(knownDeployment2).setJiraIssue(jiraIssue2);
      oneOf(knownDeployment2).setGitBranch(gitBranch2);

      oneOf(newDeployment).getIdentifier();
      will(returnValue(NEW_ID_1));
      oneOf(newDeployment).setJiraIssue(jiraIssue3);
      oneOf(newDeployment).setGitBranch(gitBranch3);

      oneOf(badNewDeployment).getIdentifier();
        will(returnValue(BAD_NEW_ID));
    } });
  }

  private void setJiraIssueRetrieverExpectations() {
    context.checking(new Expectations() { {
      oneOf(jiraIssueRetriever).getIssueDetails(KNOWN_ID_2);
        will(returnValue(jiraIssue2));
      oneOf(jiraIssueRetriever).getIssueDetails(NEW_ID_1);
        will(returnValue(jiraIssue3));
    } });
  }

  private void setGitBranchRetrieverExpectations() {
    context.checking(new Expectations() { {
      oneOf(gitBranchRetrievalService).refresh();
      oneOf(gitBranchRetrievalService).retrieveGitBranch(KNOWN_ID_2);
        will(returnValue(gitBranch2));
      oneOf(gitBranchRetrievalService).retrieveGitBranch(NEW_ID_1);
        will(returnValue(gitBranch3));
    } });
  }
}
