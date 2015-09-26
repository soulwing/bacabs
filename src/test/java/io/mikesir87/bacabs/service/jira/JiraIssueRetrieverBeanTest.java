package io.mikesir87.bacabs.service.jira;

import io.mikesir87.bacabs.JiraIssue;
import io.mikesir87.bacabs.service.jira.client.JiraClient;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jmock.Expectations.same;

/**
 * Unit test for the {@link JiraIssueRetrieverBean}
 */
public class JiraIssueRetrieverBeanTest {

  private static final String ISSUE_BASE = "http://jira.example.com/";
  private static final String JIRA_ID_PATTERN = "test-";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private JiraClient jiraClient;

  private JiraIssueRetrieverBean bean = new JiraIssueRetrieverBean();

  @Before
  public void setup() {
    bean.jiraClient = jiraClient;
    bean.jiraIssueBase = ISSUE_BASE;
    bean.jiraIdPattern = JIRA_ID_PATTERN;
  }

  @Test
  public void testGetIssueDetails() {
    String identifier = "test-identifier";
    final String url = ISSUE_BASE + identifier;
    final JiraIssue issue = context.mock(JiraIssue.class);
    context.checking(new Expectations() { {
      oneOf(jiraClient).fetchDetails(url);
      will(returnValue(issue));
    } });

    assertThat(bean.getIssueDetails(identifier), is(same(issue)));
  }

  @Test
  public void testGetIssueDetailsForNoncaringIdentifier() {
    String identifier = "some-other-identifier";
    assertThat(bean.getIssueDetails(identifier), is(nullValue()));
  }

}
