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

import java.io.IOException;

/**
 * A service for logging into a CAS authenticated web service. 
 *
 * @author Christopher M. Dunavant
 */
public interface JiraLoginService {
  
  /**
   * With a 
   * @param identifier
   * @return
   * @throws IOException
   */
  String getAuthenticatedRestUrl(String identifier) throws IOException;
  
  /**
   * With the TGT localRepoLocation and service url this will get the SGT
   * @param tgtLocation
   * @param serviceUrl
   * @return
   * @throws IOException
   */
  String getServiceGrantingTicket(String tgtLocation, String serviceUrl) throws IOException;
  
  /**
   * Gets the TGT for the given username and password
   * @param username
   * @param password
   * @return
   * @throws IOException
   */
  String getTicketGrantingTicket(String username, String password) throws IOException;
}
