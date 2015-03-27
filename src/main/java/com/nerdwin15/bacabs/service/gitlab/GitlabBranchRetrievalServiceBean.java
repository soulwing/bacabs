/*
 * File created on Mar 21, 2015 
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
package com.nerdwin15.bacabs.service.gitlab;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.nerdwin15.bacabs.ConcreteGitlabBranch;
import com.nerdwin15.bacabs.GitlabBranch;

/**
 * Implementation of {@link GitlabBranchRetrievalService}
 *
 * @author Christopher M. Dunavant
 */
@ApplicationScoped
public class GitlabBranchRetrievalServiceBean
    implements GitlabBranchRetrievalService {

  @Inject @org.soulwing.cdi.properties.Property
  protected String gitlabRestUrl;

  @Inject @org.soulwing.cdi.properties.Property
  protected String gitlabPrivateToken;
  
  @Inject @GitlabClient
  protected Client client;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabBranch retrieveGitlabBranch(String branch) {

    URI Uri = UriBuilder.fromUri(gitlabRestUrl + branch).
        queryParam("private_token", gitlabPrivateToken).build();
    try {
      return client.target(Uri).
        request(MediaType.APPLICATION_JSON).get(ConcreteGitlabBranch.class);
    }
    catch (NotFoundException e) {
      //System.out.println("Not found for: " + gitlabRestUrl + branch);
      return null;
    }
  }

}
