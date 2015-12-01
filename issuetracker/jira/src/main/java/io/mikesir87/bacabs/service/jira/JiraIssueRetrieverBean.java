package io.mikesir87.bacabs.service.jira;

import io.mikesir87.bacabs.JiraIssue;
import io.mikesir87.bacabs.service.jira.client.JiraClient;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * A CDI-injectable {@link JiraIssueRetriever} that builds the url
 * and delegates to a {@link JiraClient}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class JiraIssueRetrieverBean implements JiraIssueRetriever {

  @Inject
  @Property
  protected String jiraIssueBase;

  @Inject
  @Property
  protected String jiraIdPattern;

  @Inject
  protected JiraClient jiraClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraIssue getIssueDetails(String identifier) {
    if (!identifier.contains(jiraIdPattern))
      return null;
    String url = jiraIssueBase + identifier;
    return jiraClient.fetchDetails(url);
  }

}
