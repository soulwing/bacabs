/*
 * File created on Aug 28, 2014 
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
package io.mikesir87.bacabs.web.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mikesir87.bacabs.event.topic.Subscriber;
import io.mikesir87.bacabs.event.topic.SubscriberDisconnectedException;

import javax.websocket.Session;
import java.io.IOException;

/**
 * An implementation of a {@link Subscriber} that represents a WebSocket 
 * connected client.
 *
 * @author Michael Irwin
 * @author Chris Dunavant
 */
public class WebsocketSubscriber implements Subscriber {

  private ObjectMapper objectMapper = new ObjectMapper();
  
  protected Session session;

  public WebsocketSubscriber(Session session) {
    this.session = session;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(Object message)
      throws SubscriberDisconnectedException {
    if (session == null) {
      throw new SubscriberDisconnectedException();
    }
    try {
      String json = objectMapper.writeValueAsString(message);
      session.getBasicRemote().sendText(json);
    } 
    catch (IOException e) {
      session = null;
      throw new SubscriberDisconnectedException();
    }
  }

  public Session getSession() {
    return session;
  }
}
