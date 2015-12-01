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
  @Property(name = "docker.usingDockerMachine")
  private boolean usingDockerMachine;

  @Inject
  @Property(name = "docker.machineName")
  protected String machine;

  @Inject
  @Property(name = "docker.hostIp")
  protected String dockerHostIp;

  @Inject
  @Property(name = "docker.hostApiPort")
  protected Integer apiPort;

  @Inject
  @Property(name = "docker.certPath")
  protected String certPath;

  @Produces
  @ApplicationScoped
  public DockerConfig dockerConfig() {
    if (!usingDockerMachine)
      return new DockerConfigImpl("127.0.0.1", apiPort);

    // Using docker-machine, so look up IP and cert path for the
    DockerMachine dockerMachine = new DockerMachine(new CommandLineExecutor(), machine);
    String ip = dockerMachine.ip(false);

    String certPath = HomeResolverUtil
        .resolveHomeDirectoryChar(String.format(CERT_PATH, machine));

    return new DockerConfigImpl(ip, certPath, machine);
  }

  public static void main(String[] args) {
    DockerConfigProducer producer = new DockerConfigProducer();
    producer.runningLocally = false;
    producer.machine = "default";
    DockerConfig config = producer.dockerConfig();
    System.out.println(config.getDockerCertPath());
  }
}
