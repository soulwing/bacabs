package com.nerdwin15.bacabs.service.jira;

import com.nerdwin15.bacabs.JiraIssue;

/**
 * A service that is used to fetch the details related to a specific JIRA
 * issue.
 *
 * @author Michael Irwin
 */
public interface JiraIssueRetriever {

  /**
   * Get the issue details for the provided identifier
   * @param identifier The JIRA issue identifier
   * @return The issue details. Null if no details found.
   */
  JiraIssue getIssueDetails(String identifier);
}
