package io.mikesir87.bacabs.domain;

import io.mikesir87.bacabs.JiraIssue;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link ConcreteJiraIssueBuilder} class.
 *
 * @author Michael Irwin
 */
public class ConcreteJiraIssueBuilderTest {

  private static final String ACCEPTANCE_TASK_STATUS = "Open";
  private static final String DESCRIPTION = "Description";
  private static final Integer PERCENT_COMPLETED = 75;
  private static final String STATUS = "In Progress";
  private static final Integer SUBTASK_COUNT = 3;
  private static final String SUMMARY = "Summary";
  public ConcreteJiraIssueBuilder builder = new ConcreteJiraIssueBuilder();

  @Test
  public void testBuild() {
    JiraIssue issue = builder.acceptanceTaskStatus(ACCEPTANCE_TASK_STATUS)
        .description(DESCRIPTION)
        .percentCompleted(PERCENT_COMPLETED)
        .status(STATUS)
        .subtaskCount(SUBTASK_COUNT)
        .summary(SUMMARY)
        .build();

    assertThat(issue.getAcceptanceTaskStatus(), is(equalTo(ACCEPTANCE_TASK_STATUS)));
    assertThat(issue.getDescription(), is(equalTo(DESCRIPTION)));
    assertThat(issue.getPercentCompleted(), is(equalTo(PERCENT_COMPLETED)));
    assertThat(issue.getStatus(), is(equalTo(STATUS)));
    assertThat(issue.getSubtaskCount(), is(equalTo(SUBTASK_COUNT)));
    assertThat(issue.getSummary(), is(equalTo(SUMMARY)));
  }
}
