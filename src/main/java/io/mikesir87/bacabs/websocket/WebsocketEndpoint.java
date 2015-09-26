/*
 * File created on Dec 6, 2014 
 *
 * Copyright (c) 2014 Virginia Polytechnic Institute and State University
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
package io.mikesir87.bacabs.websocket;

import javax.inject.Inject;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import io.mikesir87.bacabs.event.topic.Subscriber;
import io.mikesir87.bacabs.event.topic.Topic;
import io.mikesir87.bacabs.event.topic.TopicRegistry;

/**
 * A websocket endpoint that registers the client to all topics in the topic
 * registry.
 *
 * @author Michael Irwin
 */
@ServerEndpoint("/websocket")
public class WebsocketEndpoint {
  
  @Inject
  protected WebsocketSubscriberFactory subscriberFactory;
  
  @Inject
  protected TopicRegistry topicRegistry;

  @OnOpen
  public void onOpen(Session session) {
    Subscriber subscriber = subscriberFactory.createSubscriber(session);
    for (Topic topic : topicRegistry.getAllTopics()) {
      topic.addSubscriber(subscriber);
    }
  }
  
}
