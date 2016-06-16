package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.DockerCmdExecFactoryImpl;

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
    if (dockerConfig.isRunningLocally()) {
      return DockerClientBuilder
          .getInstance("unix:///var/run/docker.sock")
          .withDockerCmdExecFactory(new DockerCmdExecFactoryImpl())
          .build();
    }

    DockerClientConfig config = DockerClientConfig
        .createDefaultConfigBuilder()
        .withDockerHost("https://" + dockerConfig.getIp() + ":2376")
        .withDockerCertPath(dockerConfig.getDockerCertPath())
        .build();
    return DockerClientBuilder.getInstance(config)
        .build();
  }
}
