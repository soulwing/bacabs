package io.mikesir87.bacabs.domain;

import io.mikesir87.bacabs.GitBranch;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link ConcreteGitBranchBuilder} class.
 *
 * @author Michael Irwin
 */
public class ConcreteGitBranchBuilderTest {

  private static final String COMMIT_AUTHOR = "author";
  private static final String COMMIT_DATE = "date";
  private static final String NAME = "name";

  private ConcreteGitBranchBuilder builder = new ConcreteGitBranchBuilder();

  @Test
  public void testBuild() {
    GitBranch branch = builder.lastCommitAuthor(COMMIT_AUTHOR)
        .lastCommitDate(COMMIT_DATE)
        .name(NAME)
        .build();

    assertThat(branch.getLastCommitAuthor(), is(equalTo(COMMIT_AUTHOR)));
    assertThat(branch.getLastCommitDate(), is(equalTo(COMMIT_DATE)));
    assertThat(branch.getName(), is(equalTo(NAME)));
  }
}
