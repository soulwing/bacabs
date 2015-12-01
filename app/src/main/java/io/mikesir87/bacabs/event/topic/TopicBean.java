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

import javax.enterprise.context.Dependent;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A bean implementation of a {@link Topic}.
 *
 * @author Michael Irwin
 */
@Dependent
public class TopicBean implements Topic {
  
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  
  private String name;
  private Set<Subscriber> subscribers = new HashSet<Subscriber>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addSubscriber(Subscriber subscriber) {
    lock.writeLock().lock();
    try {
      subscribers.add(subscriber);
    }
    finally {
      lock.writeLock().unlock();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSubscriber(Subscriber subscriber) {
    lock.writeLock().lock();
    try {
      subscribers.remove(subscriber);
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(Object message) {
    for (Subscriber subscriber : getSubscribers()) {
      try {
        subscriber.sendMessage(message);
      }
      catch (SubscriberDisconnectedException e) {
        subscribers.remove(subscriber);
      }
    }
  }
  
  public Set<Subscriber> getSubscribers() {
    lock.readLock().lock();
    try {
      return new LinkedHashSet<Subscriber>(subscribers);
    }
    finally {
      lock.readLock().unlock();
    }
  }
  
  /**
   * Sets the {@code name} property.
   * @param name the value to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
}
