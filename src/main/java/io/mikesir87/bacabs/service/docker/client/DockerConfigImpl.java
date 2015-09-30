package io.mikesir87.bacabs.service.docker.client;

/**
 * A non-mutable POJO that implements {@link DockerConfig}.
 *
 * @author Michael Irwin
 */
public class DockerConfigImpl implements DockerConfig {

  private final Integer localPort;
  private final String ip;
  private final String dockerCertPath;
  private final String dockerMachine;

  public DockerConfigImpl(Integer localPort) {
    this.localPort = localPort;
    this.ip = null;
    this.dockerCertPath = null;
    this.dockerMachine = null;
  }

  /**
   * Create a new instance
   * @param ip The ip address for the docker instance
   * @param dockerCertPath The docker certificate path
   * @param dockerMachine The name of the docker machine being used
   */
  public DockerConfigImpl(String ip, String dockerCertPath,
      String dockerMachine) {
    this.ip = ip;
    this.dockerCertPath = dockerCertPath;
    this.dockerMachine = dockerMachine;
    this.localPort = null;
  }

  @Override
  public boolean isRunningLocally() {
    return localPort != null;
  }

  @Override
  public Integer getLocalPort() {
    return localPort;
  }

  @Override
  public String getIp() {
    return ip;
  }

  @Override
  public String getDockerCertPath() {
    return dockerCertPath;
  }

  @Override
  public String getDockerMachine() {
    return dockerMachine;
  }
}
