package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import io.mikesir87.bacabs.service.docker.DockerContainerService;
import io.mikesir87.bacabs.service.docker.DockerContainer;
import io.mikesir87.bacabs.service.docker.DockerContainerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * A CDI-injectable {@link DockerContainerService} that uses docker-java to
 * retrieve information about running Docker containers.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DockerContainerServiceBean implements DockerContainerService {

  private static Logger logger =
      LoggerFactory.getLogger(DockerContainerServiceBean.class);

  @Inject
  @Property(name = "docker.hrefPattern")
  protected String hrefPattern;

  @Inject
  protected DockerClient dockerClient;

  @Inject
  protected DockerConfig dockerConfig;

  @Override
  public Set<DockerContainer> getHostedContainers() {
    Set<DockerContainer> containers = new HashSet<>();
    for (Container container : dockerClient.listContainersCmd().exec()) {
      InspectContainerResponse response = dockerClient.inspectContainerCmd(container.getId()).exec();
      String[] containerEnvConfig = response.getConfig().getEnv();
      
      String virtualHost = getEnvVariable(containerEnvConfig, "VIRTUAL_HOST");

      String href = String.format(hrefPattern, virtualHost);
      String identifier = getEnvVariable(containerEnvConfig, "BRANCH");

      logger.info("Container with id {} has host {} and branch {}", container.getId(), virtualHost, identifier);

      if (virtualHost == null || identifier == null)
        continue;

      logger.info("Adding container with href {} and identifier {}", href, identifier);
      containers.add(new DockerContainerImpl(href, identifier));
    }
    return containers;
  }

  private String getEnvVariable(String[] variables, String name) {
    for (String str : variables) {
      if (str.startsWith(name + "="))
        return str.substring(name.length() + 1);
    }
    return null;
  }

}
