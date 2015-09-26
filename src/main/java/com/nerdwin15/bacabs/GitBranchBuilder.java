package com.nerdwin15.bacabs;

/**
 * A builder that creates new {@link GitBranch} objects.  It is expected that
 * after the builder completes, its internal state is reset so a new build
 * can continue.
 *
 * @author Michael Irwin
 */
public interface GitBranchBuilder {

  /**
   * Set the name of the branch
   * @param name The name of the branch
   * @return The builder
   */
  GitBranchBuilder name(String name);

  /**
   * Set the committer name
   * @param commitAuthor The committer name
   * @return The builder
   */
  GitBranchBuilder lastCommitAuthor(String commitAuthor);

  /**
   * Set the date for the last commit's date
   * @param lastCommitDate The date of the last commit
   * @return The builder
   */
  GitBranchBuilder lastCommitDate(String lastCommitDate);

  /**
   * Build and return the new {@link GitBranch}
   * @return The newly built {@link GitBranch}
   */
  GitBranch build();
}
