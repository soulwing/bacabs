/*
 * File created on Mar 20, 2015 
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
package com.nerdwin15.bacabs.service.jira;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * A bean that produces jira issue client instances.
 *
 * @author Chris Dunavant
 */
@ApplicationScoped
public class JiraClientProducerBean {

  @Produces @ApplicationScoped @JiraClient
  public Client createClient() {
    
    return ClientBuilder.newBuilder()
        //.property("jersey.config.client.followRedirects", Boolean.TRUE)
        .register(JacksonJaxbJsonProvider.class)
        //.register(LoggingFilter.class)
        .build();
  }

}
