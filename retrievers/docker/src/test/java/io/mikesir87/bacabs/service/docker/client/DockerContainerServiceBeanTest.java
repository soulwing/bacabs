package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.ContainerPort;
import io.mikesir87.bacabs.service.docker.DockerContainer;
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

  private static final String HREF_PATTERN = "http://%s/test";
  private static final String CONTAINER1_ID = "abcdefgh";
  private static final String CONTAINER2_ID = "12345678";
  private static final String CONTAINER_1_HREF = "abc.example.org";
  private static final String CONTAINER_1_IDENTIFIER = "qa";
  private static final String[] CONTAINER1_ENV = {
      "VIRTUAL_HOST=" + CONTAINER_1_HREF, "BRANCH=" + CONTAINER_1_IDENTIFIER
  };
  private static final String[] CONTAINER2_ENV = {};

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
  protected InspectContainerCmd container1InspectCmd;

  @Mock
  protected InspectContainerResponse container1InspectResponse;

  @Mock
  protected ContainerConfig container1Config;

  @Mock
  protected InspectContainerCmd container2InspectCmd;

  @Mock
  protected InspectContainerResponse container2InspectResponse;

  @Mock
  protected ContainerConfig container2Config;

  @Mock
  protected Container container1;

  @Mock
  protected Container container2;

  private List<Container> containers = new ArrayList<>();

  private DockerContainerServiceBean service = new DockerContainerServiceBean();

  @Before
  public void setUp() {
    service.dockerClient = dockerClient;
    service.dockerConfig = dockerConfig;
    service.hrefPattern = HREF_PATTERN;

    containers.add(container1);
    containers.add(container2);
  }

  @Test
  public void testGetHostedContainers() {
    setClientExpectations();
    setContainerExpectations();

    Set<DockerContainer> containers = service.getHostedContainers();
    assertThat(containers.size(), is(equalTo(1)));
    DockerContainer container = containers.iterator().next();
    assertThat(container.getIdentifier(), is(equalTo(CONTAINER_1_IDENTIFIER)));
    assertThat(container.getHref(),
        is(equalTo(String.format(HREF_PATTERN, CONTAINER_1_HREF))));
  }

  private void setClientExpectations() {
    context.checking(new Expectations() { {
      oneOf(dockerClient).listContainersCmd();
        will(returnValue(listContainersCmd));
      oneOf(listContainersCmd).exec();
        will(returnValue(containers));

      oneOf(dockerClient).inspectContainerCmd(CONTAINER1_ID);
      will(returnValue(container1InspectCmd));
      oneOf(container1InspectCmd).exec();
      will(returnValue(container1InspectResponse));
      oneOf(container1InspectResponse).getConfig();
      will(returnValue(container1Config));
      oneOf(container1Config).getEnv();
      will(returnValue(CONTAINER1_ENV));

      oneOf(dockerClient).inspectContainerCmd(CONTAINER2_ID);
      will(returnValue(container2InspectCmd));
      oneOf(container2InspectCmd).exec();
      will(returnValue(container2InspectResponse));
      oneOf(container2InspectResponse).getConfig();
      will(returnValue(container2Config));
      oneOf(container2Config).getEnv();
      will(returnValue(CONTAINER2_ENV));
    } });
  }

  private void setContainerExpectations() {
    context.checking(new Expectations() { {
      allowing(container1).getId();
      will(returnValue(CONTAINER1_ID));

      allowing(container2).getId();
      will(returnValue(CONTAINER2_ID));
    } });
  }
}
