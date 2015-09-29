package io.mikesir87.bacabs.service.docker.client;

import io.mikesir87.bacabs.service.docker.client.util.CommandLineExecutor;
import io.mikesir87.bacabs.service.docker.client.util.DockerMachine;
import io.mikesir87.bacabs.service.docker.client.util.HomeResolverUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * A CDI Producer that generates the {@link DockerConfig}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DockerConfigProducer {

  private static final String CERT_PATH = "~/.docker/machine/machines/%s";

  @Produces
  @ApplicationScoped
  public DockerConfig dockerConfig() {
    String dockerMachineName = "default";
    DockerMachine dockerMachine = new DockerMachine(new CommandLineExecutor(), dockerMachineName);
    String ip = dockerMachine.ip(false);

    String certPath = HomeResolverUtil
        .resolveHomeDirectoryChar(String.format(CERT_PATH, dockerMachineName));

    return new DockerConfigImpl(ip, certPath, dockerMachineName);
  }

}
