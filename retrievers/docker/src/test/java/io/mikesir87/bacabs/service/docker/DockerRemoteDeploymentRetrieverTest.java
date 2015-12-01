package io.mikesir87.bacabs.service.docker;

import io.mikesir87.bacabs.Deployment;
import io.mikesir87.bacabs.DeploymentBuilder;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Test case for the {@link DockerRemoteDeploymentRetriever} class.
 *
 * @author Michael Irwin
 */
public class DockerRemoteDeploymentRetrieverTest {

  private static final String HREF1 = "href1";
  private static final String HREF2 = "href2";
  private static final String IDENTIFIER1 = "identifier1";
  private static final String IDENTIFIER2 = "identifier2";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private DeploymentBuilder deploymentBuilder;

  @Mock
  private DockerContainerService containerService;

  @Mock
  private DockerWildflyContainer container1;

  @Mock
  private DockerWildflyContainer container2;

  @Mock
  private Deployment deployment1;

  @Mock
  private Deployment deployment2;

  private Set<DockerWildflyContainer> containers = new HashSet<>();

  private DockerRemoteDeploymentRetriever retriever = new DockerRemoteDeploymentRetriever();

  @Before
  public void setUp() {
    retriever.containerService = containerService;
    retriever.deploymentBuilder = deploymentBuilder;

    containers.add(container1);
    containers.add(container2);
  }

  @Test
  public void testFetching() throws Exception {
    setContainerExpectations();
    setBuilderExpectations();

    Set<? extends Deployment> deployments = retriever.fetchDeployments();
    assertThat(deployments, containsInAnyOrder(deployment1, deployment2));
  }

  private void setContainerExpectations() {
    context.checking(new Expectations() {
      {
        oneOf(containerService).getWildflyContainers();
        will(returnValue(containers));

        oneOf(container1).getHref();
        will(returnValue(HREF1));
        oneOf(container1).getIdentifier();
        will(returnValue(IDENTIFIER1));

        oneOf(container2).getHref();
        will(returnValue(HREF2));
        oneOf(container2).getIdentifier();
        will(returnValue(IDENTIFIER2));
      }
    });
  }

  private void setBuilderExpectations() {
    context.checking(new Expectations() { {
      oneOf(deploymentBuilder).identifier(IDENTIFIER1);
      will(returnValue(deploymentBuilder));
      oneOf(deploymentBuilder).href(HREF1);
      will(returnValue(deploymentBuilder));
      oneOf(deploymentBuilder).build();
      will(returnValue(deployment1));

      oneOf(deploymentBuilder).identifier(IDENTIFIER2);
      will(returnValue(deploymentBuilder));
      oneOf(deploymentBuilder).href(HREF2);
      will(returnValue(deploymentBuilder));
      oneOf(deploymentBuilder).build();
      will(returnValue(deployment2));
    } });
  }
}
