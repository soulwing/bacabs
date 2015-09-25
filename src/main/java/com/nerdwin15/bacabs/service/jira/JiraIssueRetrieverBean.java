package com.nerdwin15.bacabs.service.jira;

import com.nerdwin15.bacabs.JiraIssue;
import com.nerdwin15.bacabs.service.jira.client.ChosenClient;
import com.nerdwin15.bacabs.service.jira.client.JiraClient;
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

  @Inject @ChosenClient
  protected JiraClient jiraClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraIssue getIssueDetails(String identifier) {
    String url = jiraIssueBase + identifier;
    return jiraClient.fetchDetails(url);
  }

}
