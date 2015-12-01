package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import io.mikesir87.bacabs.service.docker.DockerWildflyContainer;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link DockerContainerServiceBean} class.
 *
 * @author Michael Irwin
 */
public class DockerContainerServiceBeanTest {

  private static final String HREF_PATTERN = "http://%s:%d/test";
  private static final Integer PRIVATE_HTTP_PORT = 8080;
  private static final String IP = "192.168.100.100";
  private static final Integer CONTAINER1_PORT1_PRIVATE = 9990;
  private static final Integer CONTAINER1_PORT2_PRIVATE = 8080;
  private static final Integer CONTAINER1_PORT2_PUBLIC = 54212;
  private static final Integer CONTAINER2_PORT1_PRIVATE = 12345;
  private static final String CONTAINER_1_LABEL = "label-1234";
  private static final String CONTAINER_2_LABEL = "label-4321";
  private static final String IDENTIFIER_LABEL = "branch";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  protected DockerConfig dockerConfig;

  @Mock
  protected DockerClient dockerClient;

  @Mock
  protected ListContainersCmd listContainersCmd;

  @Mock
  protected Container container1;

  @Mock
  protected Container container2;

  @Mock
  protected Container.Port container1Port1;

  @Mock
  protected Container.Port container1Port2;

  @Mock
  protected Container.Port container2Port1;

  protected Container.Port[] container1Ports;
  protected Container.Port[] container2Ports;

  protected Map<String, String> container1Labels = Collections.singletonMap("branch", CONTAINER_1_LABEL);
  protected Map<String, String> container2Labels = Collections.singletonMap("branch", CONTAINER_2_LABEL);

  private List<Container> containers = new ArrayList<>();

  private DockerContainerServiceBean service = new DockerContainerServiceBean();

  @Before
  public void setUp() {
    service.dockerClient = dockerClient;
    service.dockerConfig = dockerConfig;
    service.hrefPattern = HREF_PATTERN;
    service.appsPrivatePort = PRIVATE_HTTP_PORT;
    service.identifierLabel = IDENTIFIER_LABEL;

    container1Ports = new Container.Port[] { container1Port1, container1Port2 };
    container2Ports = new Container.Port[] { container2Port1 };
    containers.add(container1);
    containers.add(container2);
  }

  @Test
  public void testGetWildflyContainers() {
    setClientExpectations();
    setContainerExpectations();

    Set<DockerWildflyContainer> containers = service.getWildflyContainers();
    assertThat(containers.size(), is(equalTo(1)));
    DockerWildflyContainer container = containers.iterator().next();
    assertThat(container.getIdentifier(), is(equalTo(CONTAINER_1_LABEL)));
    assertThat(container.getHref(),
        is(equalTo(String.format(HREF_PATTERN, IP, CONTAINER1_PORT2_PUBLIC))));
  }

  private void setClientExpectations() {
    context.checking(new Expectations() { {
      allowing(dockerConfig).getIp();
      will(returnValue(IP));

      oneOf(dockerClient).listContainersCmd();
        will(returnValue(listContainersCmd));
      oneOf(listContainersCmd).exec();
        will(returnValue(containers));
    } });
  }

  private void setContainerExpectations() {
    context.checking(new Expectations() { {
      oneOf(container1).getPorts();
      will(returnValue(container1Ports));
      oneOf(container1Port1).getPrivatePort();
      will(returnValue(CONTAINER1_PORT1_PRIVATE));
      oneOf(container1Port2).getPrivatePort();
      will(returnValue(CONTAINER1_PORT2_PRIVATE));
      oneOf(container1Port2).getPublicPort();
      will(returnValue(CONTAINER1_PORT2_PUBLIC));
      oneOf(container1).getLabels();
      will(returnValue(container1Labels));


      oneOf(container2).getPorts();
      will(returnValue(container2Ports));
      oneOf(container2Port1).getPrivatePort();
      will(returnValue(CONTAINER2_PORT1_PRIVATE));
      oneOf(container2).getLabels();
      will(returnValue(container2Labels));
    } });
  }
}
