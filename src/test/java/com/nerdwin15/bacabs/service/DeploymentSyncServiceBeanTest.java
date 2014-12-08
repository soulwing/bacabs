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

import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.nerdwin15.bacabs.ConcreteDeployment;
import com.nerdwin15.bacabs.Deployment;

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

  private Deployment knownDeployment1 = createDeployment(KNOWN_ID_1);
  private Deployment knownDeployment2 = createDeployment(KNOWN_ID_2);
  private Deployment newDeployment = createDeployment(NEW_ID_1);
  private Deployment badNewDeployment = createDeployment(BAD_NEW_ID);
  
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
    
    knownDeployments.add(knownDeployment1);
    knownDeployments.add(knownDeployment2);
    
    discoveredDeployments.add(knownDeployment2);
    discoveredDeployments.add(newDeployment);
    discoveredDeployments.add(badNewDeployment);
  }
  
  @Test
  public void testSync() throws Exception {
    context.checking(new Expectations() { {
      oneOf(deploymentService).getDeployments();
        will(returnValue(knownDeployments));
      oneOf(deploymentRetriever).fetchDeployments();
        will(returnValue(discoveredDeployments));
      oneOf(deploymentService).addDeployment(newDeployment);
      oneOf(deploymentService).removeDeployment(knownDeployment1);
    } });
    
    service.performDeploymentSync();
  }
  
  
  private static ConcreteDeployment createDeployment(String identifier) {
    ConcreteDeployment deployment = new ConcreteDeployment();
    deployment.setIdentifier(identifier);
    return deployment;
  }

}
