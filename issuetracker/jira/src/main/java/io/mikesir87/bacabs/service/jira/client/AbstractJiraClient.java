package io.mikesir87.bacabs.service.jira.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.mikesir87.bacabs.JiraIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * An abstract implementation of the {@link JiraClient} that builds a JAXRS
 * {@link Client} and provides it to the extending implementations.
 *
 * @author Michael Irwin
 */
public abstract class AbstractJiraClient implements JiraClient {

  private static final Logger logger =
      LoggerFactory.getLogger(AbstractJiraClient.class);

  protected Client client;

  @Inject
  protected ResponseHandler responseHandler;

  @PostConstruct
  public void init() {
    client = ClientBuilder.newBuilder()
        .register(JacksonJaxbJsonProvider.class)
        .register(new JaxRsSlf4jAdapter(LoggerFactory.getLogger(AbstractJiraClient.class)))
        .build();
    configureClient(client);
  }

  /**
   * Provides opportunities for extending classes to configure the built client
   * @param client The client to configure
   */
  protected void configureClient(Client client) {
    // Do nothing
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraIssue fetchDetails(String url) {
    try {
      Response response = performFetch(client, url);
      try {
        return responseHandler.convertResponseToIssue(response);
      } finally {
        response.close();
      }
    } catch (ProcessingException e) {
      logger.error("Processing error:", e);
      return null;
    }
  }

  /**
   * Perform the actual fetch
   * @param client The client to use for fetching
   * @param url The url to fetch
   * @return The response from doing the fetch
   */
  protected abstract Response performFetch(Client client, String url);
}
