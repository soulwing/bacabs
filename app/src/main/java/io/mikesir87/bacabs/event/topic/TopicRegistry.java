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
package io.mikesir87.bacabs.event.topic;

import java.util.Set;

/**
 * A collection of {@link Topic} objects. The implementation must be 
 * thread-safe, as topic retrieval/creation may be happening concurrently.
 *
 * @author Michael Irwin
 */
public interface TopicRegistry {
  
  /**
   * Retrieve a topic by its name. If the topic does not exist, a new one will
   * be created.
   * @param topicName The name of the topic.
   * @return The Topic with the provided name. Will not be null.
   */
  Topic getTopicByName(String topicName);

  /**
   * Return all topics in the registry
   * @return All topics in the registry
   */
  Set<Topic> getAllTopics();

}
