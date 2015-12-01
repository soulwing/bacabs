package io.mikesir87.bacabs.web.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mikesir87.bacabs.event.topic.SubscriberDisconnectedException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test case for the {@link WebsocketSubscriber} class.
 */
public class WebsocketSubscriberTest {

  private static final Object MESSAGE = new Date();

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private Session session;

  @Mock
  private RemoteEndpoint.Basic basicRemote;

  private ObjectMapper objectMapper = new ObjectMapper();

  private WebsocketSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new WebsocketSubscriber(session);
  }

  @Test(expected = SubscriberDisconnectedException.class)
  public void testSendMessageWithNoSession() throws Exception {
    subscriber.session = null;
    subscriber.sendMessage(MESSAGE);
  }

  @Test
  public void testSendMessageWhenConnected() throws Exception {
    final String dateString = objectMapper.writeValueAsString(MESSAGE);
    context.checking(new Expectations() { {
      oneOf(session).getBasicRemote();
        will(returnValue(basicRemote));
      oneOf(basicRemote).sendText(dateString);
    } });

    subscriber.sendMessage(MESSAGE);
  }

  @Test
  public void testSendMessageWhenIOExceptionThrown() throws Exception {
    context.checking(new Expectations() { {
      oneOf(session).getBasicRemote();
      will(returnValue(basicRemote));
      oneOf(basicRemote).sendText(with(any(String.class)));
      will(throwException(new IOException()));
    } });

    thrown.expect(SubscriberDisconnectedException.class);
    subscriber.sendMessage(MESSAGE);
    assertThat(subscriber.getSession(), is(nullValue()));
  }
}
