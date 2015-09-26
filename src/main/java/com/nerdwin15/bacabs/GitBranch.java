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
package com.nerdwin15.bacabs;

/**
 * Provides metadata about a specific branch.
 *
 * @author Michael Irwin
 */
public interface GitBranch {

  /**
   * Get the name of the branch
   * @return The name of the branch
   */
  String getName();

  /**
   * Get the name of the committer for the branch
   * @return
   */
  String getLastCommitAuthor();
  
  /**
   * Get the date of the last commit for the branch
   * @return date
   */
  String getLastCommitDate();
}
