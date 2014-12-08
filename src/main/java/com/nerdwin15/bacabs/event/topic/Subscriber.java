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
package com.nerdwin15.bacabs.event.topic;

/**
 * Defines an abstraction for a subscriber that is interested in hearing 
 * messages from a Topic.
 *
 * @author Michael Irwin
 * @author Chris Dunavant
 */
public interface Subscriber {

  /**
   * Send a message to the subscriber
   * @param message The message to send
   * @throws SubscriberDisconnectedException Thrown if the subscriber has
   * disconnected and is no longer available to receive messages
   */
  void sendMessage(Object message) throws SubscriberDisconnectedException;

}
