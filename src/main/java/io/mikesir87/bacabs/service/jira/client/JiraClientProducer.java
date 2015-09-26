package io.mikesir87.bacabs.service.jira.client;

import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * A producer that creates a JIRA client
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class JiraClientProducer {

  @Inject
  @Property
  protected String authType;

  @Inject
  protected Instance<CasJiraClient> casJiraClient;

  @Inject
  protected Instance<BasicAuthJiraClient> basicAuthJiraClient;

  @ApplicationScoped
  @Produces @ChosenClient
  public JiraClient produceJiraClient() {
    switch (authType.toLowerCase()) {
      case "cas" :
        return casJiraClient.get();
      case "basic" :
        return basicAuthJiraClient.get();
      default :
        throw new RuntimeException("Unable to setup client - unknown auth type");
    }
  }
}
