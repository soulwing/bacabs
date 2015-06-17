/*
 * File created on Dec 5, 2014 
 *
 * Copyright (c) 2014 Nerdwin15, LLC
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

import java.util.Date;

/**
 * Describes a deployment
 *
 * @author Michael Irwin
 */
public interface Deployment {

  /**
   * Get the identifier for the deployment
   * @return The identifier
   */
  String getIdentifier();
  
  /**
   * Get the href for the deployment
   * @return The href
   */
  String getHref();
  
  /**
   * Get the summary for the deployment
   * @return The summary
   */
  String getSummary();
  
  /**
   * Get the Jira issue associated with this deployment
   * @ return the jiraIssue
   */
  JiraIssue getJiraIssue();
  
  /**
   * Get the Gitlab branch associated with this deployment
   * @ return the gitlabBranch
   */
  GitBranch getGitBranch();

  /**
   * Get the time in which this deployment was discovered
   * @return The discovery time
   */
  Date getDiscoveryTime();
}
