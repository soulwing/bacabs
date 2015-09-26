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
package io.mikesir87.bacabs.event.listener;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.mikesir87.bacabs.event.ClientEvent;
import io.mikesir87.bacabs.event.topic.Topic;
import io.mikesir87.bacabs.event.topic.TopicRegistry;

/**
 * An event listener that relays various event messages into the 
 * topic event workflow.
 *
 * @author Michael Irwin
 */
@Startup
@Singleton
public class EventToTopicRelay {
  
  static final String ALL_EVENTS_TOPIC = "event.all";
  
  @Inject
  protected TopicRegistry topicRegistry;
  
  private Topic topic;
  
  @PostConstruct
  public void init() {
    topic = topicRegistry.getTopicByName(ALL_EVENTS_TOPIC);
  }

  public void onEvent(@Observes ClientEvent event) {
    topic.send(event);
  }

}
