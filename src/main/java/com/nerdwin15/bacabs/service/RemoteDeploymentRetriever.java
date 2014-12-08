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
package com.nerdwin15.bacabs.service;

import java.io.IOException;
import java.util.Set;

import com.nerdwin15.bacabs.Deployment;

/**
 * Defines a service that is used to sync the deployments by asking the JBoss
 * management.
 *
 * @author Michael Irwin
 */
public interface RemoteDeploymentRetriever {

  /**
   * Get the various deployments that are currently found in the container
   * @return The deployments found in the container
   */
  Set<? extends Deployment> fetchDeployments() throws IOException;
}
