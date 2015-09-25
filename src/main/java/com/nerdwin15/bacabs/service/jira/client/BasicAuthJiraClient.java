package com.nerdwin15.bacabs.service.jira.client;

import com.nerdwin15.bacabs.ConcreteJiraIssue;
import com.nerdwin15.bacabs.JiraIssue;
import org.apache.commons.lang.Validate;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * An {@link AbstractJiraClient} that is configured to use BASIC
 * auth for authentication.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class BasicAuthJiraClient extends AbstractJiraClient {

  @Inject
  @Property
  protected String username;

  @Inject
  @Property
  protected String password;

  private String authValue;

  @Override
  protected void configureClient(Client client) {
    Validate.notEmpty(username, "jira username must be configured");
    Validate.notEmpty(password, "jira password must be configured");
    authValue = getBasicAuthentication(username, password);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JiraIssue performFetch(Client client, String url) {
    try {
      return client.target(url)
          .request(MediaType.APPLICATION_JSON)
          .header("Authorization", authValue)
          .get(ConcreteJiraIssue.class);
    } catch (NotFoundException e) {
      return null;
    }
  }

  private String getBasicAuthentication(String user, String password) {
    String token = user + ":" + password;
    try {
      return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Cannot encode with UTF-8", ex);
    }
  }

}
