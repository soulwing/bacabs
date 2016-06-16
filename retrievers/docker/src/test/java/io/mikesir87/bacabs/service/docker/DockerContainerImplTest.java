package io.mikesir87.bacabs.service.docker;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link DockerContainerImpl} class.
 *
 * @author Michael Irwin
 */
public class DockerContainerImplTest {

  private static final String IDENTIFIER = "an-identifier";
  private static final String HREF = "some-href:1234";
  private DockerContainerImpl container;

  @Test
  public void testContainer() {
    container = new DockerContainerImpl(HREF, IDENTIFIER);
    assertThat(container.getHref(), is(equalTo(HREF)));
    assertThat(container.getIdentifier(), is(equalTo(IDENTIFIER)));
  }
}
