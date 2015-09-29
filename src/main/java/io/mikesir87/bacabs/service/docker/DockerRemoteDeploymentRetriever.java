package io.mikesir87.bacabs.service.docker;

import io.mikesir87.bacabs.Deployment;
import io.mikesir87.bacabs.DeploymentBuilder;
import io.mikesir87.bacabs.service.RemoteDeploymentRetriever;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link RemoteDeploymentRetriever} that looks for various Docker images.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class DockerRemoteDeploymentRetriever implements RemoteDeploymentRetriever {

  @Inject
  protected DeploymentBuilder deploymentBuilder;

  @Inject
  protected DockerContainerService containerService;

  @Override
  public Set<? extends Deployment> fetchDeployments() throws IOException {
    Set<Deployment> deployments = new HashSet<>();
    for (DockerWildflyContainer container : containerService.getWildflyContainers()) {
      deployments.add(
          deploymentBuilder
              .identifier(container.getIdentifier())
              .href(container.getHref())
              .build()
      );
    }
    return deployments;
  }

}
