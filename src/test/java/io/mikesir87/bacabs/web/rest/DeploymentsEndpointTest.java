package io.mikesir87.bacabs.web.rest;

import io.mikesir87.bacabs.Deployment;
import io.mikesir87.bacabs.service.DeploymentService;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Test case for the {@link DeploymentsEndpoint} class.
 */
public class DeploymentsEndpointTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  DeploymentService deploymentService;

  private DeploymentsEndpoint endpoint = new DeploymentsEndpoint();

  @Before
  public void setUp() {
    endpoint.deploymentService = deploymentService;
  }

  @Test
  public void testGetDeployment() throws Exception {
    final Set<Deployment> deployments = new HashSet<>();
    context.checking(new Expectations() {
      {
        oneOf(deploymentService).getDeployments();
        will(returnValue(deployments));
      }
    });

    Response response = endpoint.getDeployments();
    assertThat(response.getStatus(), is(200));
    assertThat(response.getEntity(), is(sameInstance((Object) deployments)));
  }
}
