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
package com.nerdwin15.bacabs.service.jira.client;

import com.nerdwin15.bacabs.JiraIssue;

/**
 * A definition for a client that can request JIRA details. It encapsulates
 * the details around authentication.
 *
 * @author Michael Irwin
 */
public interface JiraClient {

  /**
   * Fetch the details for the JIRA issue found at the provided url
   * @param url The url for the issue details
   * @return The issue details
   */
  JiraIssue fetchDetails(String url);
}