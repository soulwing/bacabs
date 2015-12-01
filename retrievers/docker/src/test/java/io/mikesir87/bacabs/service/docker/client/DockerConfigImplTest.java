package io.mikesir87.bacabs.service.docker.client;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link DockerConfigImpl} class.
 *
 * @author Michael Irwin
 */
public class DockerConfigImplTest {

  private static final String DOCKER_MACHINE = "default";
  private static final String CERT_PATH = "/some/path/goes/here";
  private static final String IP = "129.168.100.100";

  @Test
  public void testConfig() {
    DockerConfigImpl config = new DockerConfigImpl(IP, CERT_PATH, DOCKER_MACHINE);
    assertThat(config.getDockerCertPath(), is(equalTo(CERT_PATH)));
    assertThat(config.getDockerMachine(), is(equalTo(DOCKER_MACHINE)));
    assertThat(config.getIp(), is(equalTo(IP)));
  }
}
