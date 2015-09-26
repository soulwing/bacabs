package io.mikesir87.bacabs.service.jira.client;

import org.apache.commons.lang.Validate;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
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
  protected Response performFetch(Client client, String url) {
    return client.target(url)
        .request(MediaType.APPLICATION_JSON)
        .header("Authorization", authValue)
        .get();
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
