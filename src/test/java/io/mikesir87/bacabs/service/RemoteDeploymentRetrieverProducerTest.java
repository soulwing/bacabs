package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.service.docker.DockerRemoteDeploymentRetriever;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.enterprise.inject.Instance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Test case for the {@link RemoteDeploymentRetrieverProducer} class.
 *
 * @author Michael Irwin
 */
public class RemoteDeploymentRetrieverProducerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private Instance<WildflyRemoteDeploymentRetriever> wildflyRetrieverInstance;

  private WildflyRemoteDeploymentRetriever wildflyRetriever =
      new WildflyRemoteDeploymentRetriever();

  @Mock
  private Instance<DockerRemoteDeploymentRetriever> dockerRetrieverInstance;

  private DockerRemoteDeploymentRetriever dockerRetriever =
      new DockerRemoteDeploymentRetriever();

  private RemoteDeploymentRetrieverProducer producer =
      new RemoteDeploymentRetrieverProducer();

  @Before
  public void setUp() {
    producer.dockerRetriever = dockerRetrieverInstance;
    producer.wildflyRetriever = wildflyRetrieverInstance;
  }

  @Test
  public void testWildflySetting() {
    producer.retrieverOption = RemoteDeploymentRetrieverProducer.WILDFLY;
    context.checking(new Expectations() { {
      oneOf(wildflyRetrieverInstance).get();
      will(returnValue(wildflyRetriever));
    } });

    assertThat(producer.remoteDeploymentRetriever(),
        is(sameInstance((Object) wildflyRetriever)));
  }

  @Test
  public void testDockerSetting() {
    producer.retrieverOption = RemoteDeploymentRetrieverProducer.DOCKER;
    context.checking(new Expectations() { {
      oneOf(dockerRetrieverInstance).get();
      will(returnValue(dockerRetriever));
    } });

    assertThat(producer.remoteDeploymentRetriever(),
        is(sameInstance((Object) dockerRetriever)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSetting() {
    producer.retrieverOption = "Something else";
    producer.remoteDeploymentRetriever();
  }
}
