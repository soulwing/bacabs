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
package com.nerdwin15.bacabs.service.git;

import com.nerdwin15.bacabs.GitBranch;

/**
 * A service that is used to retrieve a branches from a Git
 * repository.
 *
 * @author Chris Dunavant
 * @author Carl Harris
 */
public interface GitBranchRetrievalService {

  /**
   * Refreshes the repository (e.g. by invoking fetch to retrieve commits
   * from a remote repository).
   */
  void refresh();

  /**
   * Retrieve the information for a given Git branch
   * @param branch the subject branch
   */
  GitBranch retrieveGitBranch(String branch);

}
