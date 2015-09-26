/*
 * File created on Dec 6, 2014 
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
package io.mikesir87.bacabs.service;

import java.io.IOException;

/**
 * Defines a service that is used to perform the sync of the local deployment
 * repository with that of the container.
 *
 * @author Michael Irwin
 */
public interface DeploymentSyncService {

  /**
   * Perform the sync of locally known deployments with those found in the
   * container.
   * @throws IOException Thrown if communication occurs while trying to talk
   * to the remote container
   */
  void performDeploymentSync() throws IOException;
}
