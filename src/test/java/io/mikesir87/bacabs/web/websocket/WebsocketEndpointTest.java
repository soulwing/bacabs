/*
 * File created on Dec 8, 2014 
 *
 * Copyright (c) 2014 Nerdwin15, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.mikesir87.bacabs.web.websocket;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import io.mikesir87.bacabs.event.topic.TopicRegistry;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mikesir87.bacabs.event.topic.Topic;

/**
 * Unit test for the {@link WebsocketEndpoint}.
 *
 * @author Michael Irwin
 */
public class WebsocketEndpointTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  } };

  @Mock Session session;
  @Mock Topic topic1;
  @Mock Topic topic2;
  @Mock
  TopicRegistry topicRegistry;
  @Mock WebsocketSubscriber subscriber;
  @Mock WebsocketSubscriberFactory subscriberFactory;

  private Set<Topic> topics = new HashSet<>();

  private WebsocketEndpoint endpoint = new WebsocketEndpoint();

  /**
   * Perform setup tasks
   */
  @Before
  public void setUp() throws Exception {
    topics.add(topic1);
    topics.add(topic2);
    endpoint.subscriberFactory = subscriberFactory;
    endpoint.topicRegistry = topicRegistry;
  }
  
  @Test
  public void testOnOpen() {
    context.checking(new Expectations() { {
      oneOf(subscriberFactory).createSubscriber(session);
        will(returnValue(subscriber));
      oneOf(topicRegistry).getAllTopics();
        will(returnValue(topics));
      oneOf(topic1).addSubscriber(subscriber);
      oneOf(topic2).addSubscriber(subscriber);
    } });
    
    endpoint.onOpen(session);
  }

}
