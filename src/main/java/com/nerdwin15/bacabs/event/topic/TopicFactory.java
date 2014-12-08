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
 * Defines a factory that can create {@link Topic} objects.
 *
 * @author Michael Irwin
 * @author Chris Dunavant
 */
interface TopicFactory {
  
  /**
   * Create a new topic, based on the provided name
   * @param topicName The name of the topic
   * @return A configured topic
   */
  Topic createTopic(String topicName);

}
