package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.JiraIssue;
import io.mikesir87.bacabs.domain.ConcreteJiraIssue;
import io.mikesir87.bacabs.service.jira.JiraIssueRetriever;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by mikesir on 5/12/16.
 */
@ApplicationScoped
public class NoopJiraIssueRetrieverBean implements JiraIssueRetriever {

  @Override
  public JiraIssue getIssueDetails(String identifier) {
    return new ConcreteJiraIssue();
  }
}
