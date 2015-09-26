package io.mikesir87.bacabs.web.websocket;

import io.mikesir87.bacabs.event.topic.Subscriber;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.websocket.Session;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Test case for the {@link WebsocketSubscriberFactoryBean} class.
 *
 * @author Michael irwin
 */
public class WebsocketSubscriberFactoryBeanTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private WebsocketSubscriberFactory factory = new WebsocketSubscriberFactoryBean();

  @Test
  public void testCreateSubscriber() {
    Session session = context.mock(Session.class);
    Subscriber subscriber = factory.createSubscriber(session);
    assertThat(subscriber, instanceOf(WebsocketSubscriber.class));
    assertThat(((WebsocketSubscriber) subscriber).getSession(), is(sameInstance(session)));
  }
}
