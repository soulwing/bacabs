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

import io.mikesir87.bacabs.event.topic.Subscriber;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 * A CDI-injectable {@link WebsocketSubscriberFactory}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class WebsocketSubscriberFactoryBean
    implements WebsocketSubscriberFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public Subscriber createSubscriber(Session session) {
    return new WebsocketSubscriber(session);
  }
  
}
