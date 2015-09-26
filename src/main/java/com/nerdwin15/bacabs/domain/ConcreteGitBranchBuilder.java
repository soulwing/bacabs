package com.nerdwin15.bacabs.domain;

import com.nerdwin15.bacabs.GitBranch;
import com.nerdwin15.bacabs.GitBranchBuilder;

import javax.enterprise.context.Dependent;

/**
 * A {@link GitBranchBuilder} that creates {@link ConcreteGitBranch} objects.
 *
 * @author Michael Irwin
 */
@Dependent
public class ConcreteGitBranchBuilder implements GitBranchBuilder {

  private ConcreteGitBranch delegate = new ConcreteGitBranch();

  @Override
  public GitBranchBuilder name(String name) {
    delegate.setName(name);
    return this;
  }

  @Override
  public GitBranchBuilder lastCommitAuthor(String commitAuthor) {
    delegate.setLastCommitAuthor(commitAuthor);
    return this;
  }

  @Override
  public GitBranchBuilder lastCommitDate(String lastCommitDate) {
    delegate.setLastCommitDate(lastCommitDate);
    return this;
  }

  @Override
  public GitBranch build() {
    GitBranch returningBranch = this.delegate;
    this.delegate = new ConcreteGitBranch();
    return returningBranch;
  }
}
