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

import com.nerdwin15.bacabs.GitlabBranch;

/**
 * A service that is used to retrieve a branch from a Gitlab
 * REST endpoint.
 *
 * @author Chris Dunavant
 */
public interface GitlabBranchRetrievalService {

  /**
   * Retrieve the information for a given Gitlab branch
   * @param url The authenticated REST url
   */
  GitlabBranch retrieveGitlabBranch(String branch);
}
