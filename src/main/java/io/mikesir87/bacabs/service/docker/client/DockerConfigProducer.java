package io.mikesir87.bacabs.service.docker.client;

import io.mikesir87.bacabs.service.docker.client.util.CommandLineExecutor;
import io.mikesir87.bacabs.service.docker.client.util.DockerMachine;
import io.mikesir87.bacabs.service.docker.client.util.HomeResolverUtil;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * A CDI Producer that generates the {@link DockerConfig}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DockerConfigProducer {

  private static final String CERT_PATH = "~/.docker/machine/machines/%s";

  @Inject
  @Property
  protected String machine;

  @Inject
  @Property
  protected Integer localDockerPort;

  @Produces
  @ApplicationScoped
  public DockerConfig dockerConfig() {
    if (machine.equals("local"))
      return new DockerConfigImpl(localDockerPort);

    DockerMachine dockerMachine = new DockerMachine(new CommandLineExecutor(), machine);
    String ip = dockerMachine.ip(false);

    String certPath = HomeResolverUtil
        .resolveHomeDirectoryChar(String.format(CERT_PATH, machine));

    return new DockerConfigImpl(ip, certPath, machine);
  }

}
