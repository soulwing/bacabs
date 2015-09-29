package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.service.docker.DockerRemoteDeploymentRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * A producer that determines the {@link RemoteDeploymentRetriever} that should
 * be used, based on property configuration in the application.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class RemoteDeploymentRetrieverProducer {

  private static final Logger logger =
      LoggerFactory.getLogger(RemoteDeploymentRetrieverProducer.class);

  protected static final String DOCKER = "docker";
  protected static final String WILDFLY = "wildfly";

  @Inject @Property
  protected String retrieverOption;

  @Inject
  protected Instance<WildflyRemoteDeploymentRetriever> wildflyRetriever;

  @Inject
  protected Instance<DockerRemoteDeploymentRetriever> dockerRetriever;

  @Produces
  @ApplicationScoped
  @ChosenPerConfiguration
  public RemoteDeploymentRetriever remoteDeploymentRetriever() {
    retrieverOption = retrieverOption.toLowerCase();
    if (retrieverOption.equals(DOCKER)) {
      logger.info("Using DOCKER retriever");
      return dockerRetriever.get();
    }
    else if (retrieverOption.equals(WILDFLY)) {
      logger.info("Using WILDFLY retriever");
      return wildflyRetriever.get();
    }
    throw new IllegalArgumentException("Unknown retrieverOption: " + retrieverOption);
  }

}
