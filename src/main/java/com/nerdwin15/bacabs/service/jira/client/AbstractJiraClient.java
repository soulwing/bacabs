package com.nerdwin15.bacabs.service.jira.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.nerdwin15.bacabs.JiraIssue;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * An abstract implementation of the {@link JiraClient} that builds a JAXRS
 * {@link Client} and provides it to the extending implementations.
 *
 * @author Michael Irwin
 */
public abstract class AbstractJiraClient implements JiraClient {

  protected Client client;

  @PostConstruct
  public void init() {
    client = ClientBuilder.newBuilder()
        .register(JacksonJaxbJsonProvider.class)
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
    return performFetch(client, url);
  }

  /**
   * Perform the actual fetch
   * @param client The client to use for fetching
   * @param url The url to fetch
   * @return The JIRA issue details
   */
  protected abstract JiraIssue performFetch(Client client, String url);
}
