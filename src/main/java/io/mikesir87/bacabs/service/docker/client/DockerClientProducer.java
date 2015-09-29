package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * A CDI Producer that creates a {@link DockerClient} based on the
 * {@link DockerConfig} configuration.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DockerClientProducer {

  @Inject
  protected DockerConfig dockerConfig;

  /**
   * Produce a DockerClient, based on the injected {@link DockerConfig}
   * @return A configured DockerClient
   */
  @Produces
  @ApplicationScoped
  public DockerClient dockerClient() {
    DockerClientConfig config = DockerClientConfig
        .createDefaultConfigBuilder()
        .withUri("https://" + dockerConfig.getIp() + ":2376")
        .withDockerCertPath(dockerConfig.getDockerCertPath())
        .build();

    return DockerClientBuilder.getInstance(config)
        .build();
  }
}
