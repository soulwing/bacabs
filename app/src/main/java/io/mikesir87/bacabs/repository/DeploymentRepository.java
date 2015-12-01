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
package io.mikesir87.bacabs.repository;

import io.mikesir87.bacabs.Deployment;

import java.util.Set;

/**
 * A definition for a repository of {@link Deployment} objects.
 *
 * @author Michael Irwin
 */
public interface DeploymentRepository {

  /**
   * Get all deployments
   * @return All deployments
   */
  Set<? extends Deployment> getDeployments();
  
  /**
   * Add a deployment
   * @param deployment The deployment to add
   */
  void addDeployment(Deployment deployment);

  /**
   * Merge the provided deployment
   * @param deployment The deployment to merge
   * @return The updated deployment. Not guaranteed to be the same object
   * as that passed in.
   */
  Deployment updateDeployment(Deployment deployment);
  
  /**
   * Remove a deployment
   * @param deployment The deployment to remove
   */
  void removeDeployment(Deployment deployment);
}
