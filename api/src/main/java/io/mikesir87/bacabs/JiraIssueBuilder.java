package io.mikesir87.bacabs;

/**
 * A builder for {@link JiraIssue} objects.  Once built, the builder must reset
 * its internal state so a new object can be immediately be built.
 *
 * @author Michael irwin
 */
public interface JiraIssueBuilder {

  /**
   * Set the summary for the issue
   * @param summary The summary for the issue
   * @return The builder
   */
  JiraIssueBuilder summary(String summary);

  /**
   * Set the description for the issue
   * @param description The description for the issue
   * @return The builder
   */
  JiraIssueBuilder description(String description);

  /**
   * Set the status for the overall issue
   * @param status The status for the issue
   * @return The builder
   */
  JiraIssueBuilder status(String status);

  /**
   * Set the subtaskCount property on the object
   * @param subtaskCount The number of subtasks
   * @return The builder
   */
  JiraIssueBuilder subtaskCount(Integer subtaskCount);

  /**
   * Set the percentCompleted property on the object
   * @param percentCompleted The percent completed
   * @return The builder
   */
  JiraIssueBuilder percentCompleted(Integer percentCompleted);

  /**
   * Set the acceptanceTaskStatus property on the object
   * @param acceptanceTaskStatus The acceptance task status
   * @return The builder
   */
  JiraIssueBuilder acceptanceTaskStatus(String acceptanceTaskStatus);

  /**
   * Build the final object and reset the builder for a new object to be built
   * @return The newly created {@link JiraIssue}
   */
  JiraIssue build();
}
