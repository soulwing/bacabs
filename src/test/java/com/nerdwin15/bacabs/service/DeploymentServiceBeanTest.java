/*
 * File created on Dec 6, 2014 
 *
 * Copyright (c) 2014 Virginia Polytechnic Institute and State University
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Event;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.nerdwin15.bacabs.Deployment;
import com.nerdwin15.bacabs.event.NewDeploymentEvent;
import com.nerdwin15.bacabs.event.RemovedDeploymentEvent;
import com.nerdwin15.bacabs.repository.DeploymentRepository;
import com.nerdwin15.bacabs.service.DeploymentServiceBean;

/**
 * Test case for the {@link DeploymentServiceBean}.
 *
 * @author Michael Irwin
 */
public class DeploymentServiceBeanTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock Deployment deployment;
  @Mock DeploymentRepository deploymentRepository;
  @Mock Event<NewDeploymentEvent> newDeploymentEvent;
  @Mock Event<RemovedDeploymentEvent> removedDeploymentEvent;

  private DeploymentServiceBean bean = new DeploymentServiceBean();

  /**
   * Perform setup tasks
   */
  @Before
  public void setUp() throws Exception {
    bean.deploymentRepository = deploymentRepository;
    bean.newDeploymentEvent = newDeploymentEvent;
    bean.removedDeploymentEvent = removedDeploymentEvent;
  }

  /**
   * Test the get all method
   */
  @Test
  public void testGetAll() {
    final Set<Deployment> deployments = new HashSet<>();
    context.checking(new Expectations() { { 
      oneOf(deploymentRepository).getDeployments();
        will(returnValue(deployments));
    } });
    assertThat(bean.getDeployments(), is(equalTo((Object) deployments)));
  }

  /**
   * Test the add method
   */
  @Test
  public void testAdd() {
    context.checking(new Expectations() { { 
      oneOf(deploymentRepository).addDeployment(deployment);
      oneOf(newDeploymentEvent).fire((NewDeploymentEvent) with(hasProperty("deployment", sameInstance(deployment))));
    } });
    bean.addDeployment(deployment);
  }
  
  /**
   * Test the get all method
   */
  @Test
  public void testRemove() {
    context.checking(new Expectations() { { 
      oneOf(deploymentRepository).removeDeployment(deployment);
      oneOf(removedDeploymentEvent).fire((RemovedDeploymentEvent) with(hasProperty("deployment", sameInstance(deployment))));
    } });
    bean.removeDeployment(deployment);
  }
}
