package io.mikesir87.bacabs.service.docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Filters;
import io.mikesir87.bacabs.service.docker.DockerContainerService;
import io.mikesir87.bacabs.service.docker.DockerWildflyContainer;
import io.mikesir87.bacabs.service.docker.DockerWildflyContainerImpl;
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

  @Inject
  @Property(name = "docker.hrefPattern")
  protected String hrefPattern;

  @Inject
  @Property(name = "docker.appsPrivatePort")
  protected Integer appsPrivatePort;

  @Inject
  @Property(name = "docker.identifierLabel")
  protected String identifierLabel;

  @Inject
  protected DockerClient dockerClient;

  @Inject
  protected DockerConfig dockerConfig;

  @Override
  public Set<DockerWildflyContainer> getWildflyContainers() {
    Set<DockerWildflyContainer> containers = new HashSet<>();
    for (Container container : dockerClient.listContainersCmd().exec()) {
      String href = getHref(container);
      String identifier = getIdentifier(container);
      if (href == null || identifier == null)
        continue;
      containers.add(new DockerWildflyContainerImpl(href, identifier));
    }
    return containers;
  }

  private String getHref(Container container) {
    for (Container.Port port : container.getPorts()) {
      if (port.getPrivatePort().equals(appsPrivatePort))
        return String.format(hrefPattern, dockerConfig.getIp(), port.getPublicPort());
    }
    return null;
  }

  private String getIdentifier(Container container) {
    return container.getLabels().get(identifierLabel);
  }
}
