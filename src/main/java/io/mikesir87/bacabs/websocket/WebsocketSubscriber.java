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
package io.mikesir87.bacabs.websocket;

import java.io.IOException;

import javax.enterprise.context.Dependent;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mikesir87.bacabs.event.topic.Subscriber;
import io.mikesir87.bacabs.event.topic.SubscriberDisconnectedException;

/**
 * An implementation of a {@link Subscriber} that represents a WebSocket 
 * connected client.
 *
 * @author Michael Irwin
 * @author Chris Dunavant
 */
@Dependent
public class WebsocketSubscriber implements Subscriber {

  private ObjectMapper objectMapper = new ObjectMapper();
  
  private Session session;
  
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
  
  /**
   * Gets the {@code websocketSession} property.
   * @return property value
   */
  public Session getSession() {
    return session;
  }

  /**
   * Sets the {@code websocketSession} property.
   * @param websocketSession the value to set
   */
  public void setSession(Session websocketSession) {
    this.session = websocketSession;
  }

}
