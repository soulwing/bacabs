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
 * A Topic contains a collection of {@link Subscriber}s that are interested in
 * hearing notifications/messages related to the topic instance.
 *
 * @author Michael Irwin
 */
public interface Topic {

  /**
   * Get the name of the topic. It is expected that the name of a specific 
   * topic instance will never change
   * @return The topic name
   */
  String getName();
  
  /**
   * Add a subscriber to the Topic
   * @param subscriber The subscriber to add
   */
  void addSubscriber(Subscriber subscriber);
  
  /**
   * Remove the subscriber from the Topic
   * @param subscriber The subscriber to remove
   */
  void removeSubscriber(Subscriber subscriber);
  
  /**
   * Send a message to all of the subscribers in the topic. 
   * @param message The payload of the message to be sent
   */
  void send(Object message);
}
