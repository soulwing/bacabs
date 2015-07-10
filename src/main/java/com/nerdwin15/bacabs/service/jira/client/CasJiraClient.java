package com.nerdwin15.bacabs.service.jira.client;

import com.nerdwin15.bacabs.ConcreteJiraIssue;
import com.nerdwin15.bacabs.JiraIssue;
import org.apache.commons.lang.Validate;
import org.soulwing.cdi.properties.Property;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * An {@link AbstractJiraClient} that uses CAS for authentication.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class CasJiraClient extends AbstractJiraClient {

  private NewCookie sessionCookie;

  @Inject
  @Property(name = "jira.auth.cas.username")
  private String username;

  @Inject
  @Property(name = "jira.auth.cas.password")
  private String password;

  @Inject
  @Property(name = "jira.auth.cas.url")
  private String casUrl;

  @Override
  @PostConstruct
  public void init() {
    super.init();
    Validate.notEmpty(username, "jira username must be configured");
    Validate.notEmpty(password, "jira password must be configured");
    Validate.notEmpty(casUrl, "cas url must be configured");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JiraIssue performFetch(Client client, String url) {
    if (sessionCookie == null) {
      return authenticate(client, url);
    }

    Response response = client.target(url)
        .request()
        .cookie(sessionCookie)
        .get();
    try {
      return response.readEntity(ConcreteJiraIssue.class);
    } finally {
      response.close();
    }
  }

  private JiraIssue authenticate(Client client, String jiraRequestUrl) {
    Form form = new Form();
    form.param("username", username).param("password", password);
System.out.println("(*&%(@*#%&@#(*%& CAS URL: " + casUrl);
    System.out.println("*%&*#&%*@#&% CLIENT: " + client);
    Response response = client.target(casUrl)
        .request()
        .post(Entity.form(form));
    String tgtLocation = "";
    try {
      if (response.getStatus() == 400)
        throw new RuntimeException("CAS username and password failed authentication");

      tgtLocation = response.getHeaderString("Location");
    } finally {
      response.close();
    }

    form = new Form();
    form.param("service", jiraRequestUrl);
    String casTicket = client.target(tgtLocation)
        .request()
        .post(Entity.form(form), String.class);

    response = client.target(jiraRequestUrl)
        .queryParam("ticket", casTicket)
        .request()
        .get();
    sessionCookie = response.getCookies().get("JSESSIONID");
    response.close();
    return performFetch(client, jiraRequestUrl);
  }
}
