package io.mikesir87.bacabs.domain;

import io.mikesir87.bacabs.Deployment;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link ConcreteDeploymentBuilder} class.
 *
 * @author Michael Irwin
 */
public class ConcreteDeploymentBuilderTest {

  private static final String HREF = "an-href";
  private static final String IDENTIFIER = "identifier";

  private ConcreteDeploymentBuilder builder = new ConcreteDeploymentBuilder();

  @Test
  public void testBuilder() {
    Deployment deployment = builder.identifier(IDENTIFIER)
        .href(HREF)
        .build();
    assertThat(deployment.getHref(), is(equalTo(HREF)));
    assertThat(deployment.getIdentifier(), is(equalTo(IDENTIFIER)));
  }
}
