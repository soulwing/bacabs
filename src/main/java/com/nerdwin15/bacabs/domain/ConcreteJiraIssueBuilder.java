package com.nerdwin15.bacabs.domain;

import com.nerdwin15.bacabs.JiraIssue;
import com.nerdwin15.bacabs.JiraIssueBuilder;

import javax.enterprise.context.Dependent;

/**
 * A {@link JiraIssueBuilder} that creates {@link ConcreteJiraIssue} objects.
 *
 * @author Michael Irwin
 */
@Dependent
public class ConcreteJiraIssueBuilder implements JiraIssueBuilder {

  private ConcreteJiraIssue delegate = new ConcreteJiraIssue();
  
  @Override
  public JiraIssueBuilder summary(String summary) {
    delegate.setSummary(summary);
    return this;
  }

  @Override
  public JiraIssueBuilder description(String description) {
    delegate.setDescription(description);
    return this;
  }

  @Override
  public JiraIssueBuilder status(String status) {
    delegate.setStatus(status);
    return this;
  }

  @Override
  public JiraIssueBuilder subtaskCount(Integer subtaskCount) {
    delegate.setSubtaskCount(subtaskCount);
    return this;
  }

  @Override
  public JiraIssueBuilder percentCompleted(Integer percentCompleted) {
    delegate.setPercentCompleted(percentCompleted);
    return this;
  }

  @Override
  public JiraIssueBuilder acceptanceTaskStatus(String acceptanceTaskStatus) {
    delegate.setAcceptanceTaskStatus(acceptanceTaskStatus);
    return this;
  }

  @Override
  public JiraIssue build() {
    JiraIssue returningIssue = this.delegate;
    this.delegate = new ConcreteJiraIssue();
    return returningIssue;
  }

}
