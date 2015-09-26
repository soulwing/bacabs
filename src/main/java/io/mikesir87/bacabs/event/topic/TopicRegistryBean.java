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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * An implementation of the {@link TopicRegistry}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class TopicRegistryBean implements TopicRegistry {
  
  @Inject
  protected TopicFactory topicFactory;

  private Map<String, Topic> topicList = new HashMap<>();
  private Lock lock = new ReentrantLock();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Topic> getAllTopics() {
    lock.lock();
    try {
      return new HashSet<Topic>(topicList.values());
    }
    finally {
      lock.unlock();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Topic getTopicByName(String topicName) {
    lock.lock();
    try {
      if (!topicList.containsKey(topicName)) {
        topicList.put(topicName, topicFactory.createTopic(topicName));
      }        
      return topicList.get(topicName);
    }
    finally {
      lock.unlock();
    }
  }
  
}
