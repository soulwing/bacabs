package com.nerdwin15.bacabs.service.jira;

import com.nerdwin15.bacabs.JiraIssue;
import com.nerdwin15.bacabs.service.jira.client.JiraClient;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jmock.Expectations.same;

/**
 * Unit test for the {@link JiraIssueRetrieverBean}
 */
public class JiraIssueRetrieverBeanTest {

  private static final String ISSUE_BASE = "http://jira.example.com/";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private JiraClient jiraClient;

  private JiraIssueRetrieverBean bean = new JiraIssueRetrieverBean();

  @Before
  public void setup() {
    bean.jiraClient = jiraClient;
    bean.jiraIssueBase = ISSUE_BASE;
  }

  @Test
  public void testGetIssueDetails() {
    String identifier = "some-identifier";
    final String url = ISSUE_BASE + identifier;
    final JiraIssue issue = context.mock(JiraIssue.class);
    context.checking(new Expectations() { {
      oneOf(jiraClient).fetchDetails(url);
      will(returnValue(issue));
    } });

    assertThat(bean.getIssueDetails(identifier), is(same(issue)));
  }

}