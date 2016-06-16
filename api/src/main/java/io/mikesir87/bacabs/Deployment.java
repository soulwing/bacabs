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
package io.mikesir87.bacabs;

import java.util.Date;

/**
 * Describes a deployment
 *
 * @author Michael Irwin
 */
public interface Deployment {

  enum Status {
    VERIFIED("Verified"), UNKNOWN("Unknown");

    private final String displayName;

    Status(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
  }

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
   * Get the status for the deployment. By simply having a deployment, it is
   * not 100% guaranteed that the deployment is active and ready to be accessed.
   * The status indicates whether the deployment has been confirmed to be up
   * or not.
   * @return The status for the deployment
   */
  Status getStatus();

  /**
   * Set the status for the deployment
   * @param status
   */
  void setStatus(Status status);
  
  /**
   * Get the Jira issue associated with this deployment
   * @ return the jiraIssue
   */
  JiraIssue getJiraIssue();

  /**
   * Set the JIRA issue for a deployment.
   */
  void setJiraIssue(JiraIssue jiraIssue);
  
  /**
   * Get the git branch associated with this deployment
   * @ return the git branch information
   */
  GitBranch getGitBranch();

  /**
   * Set the git branch information for the deployment
   * @param gitBranch
   */
  void setGitBranch(GitBranch gitBranch);

  /**
   * Get the time in which this deployment was discovered
   * @return The discovery time
   */
  Date getDiscoveryTime();
}
