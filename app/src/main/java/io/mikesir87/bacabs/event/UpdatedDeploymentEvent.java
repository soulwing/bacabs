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
package io.mikesir87.bacabs.event;

import io.mikesir87.bacabs.Deployment;

/**
 * Event for new deployments
 *
 * @author Michael Irwin
 */
public class UpdatedDeploymentEvent extends AbstractDeploymentEvent
    implements ClientEvent {

  private static final String TYPE = UpdatedDeploymentEvent.class.getSimpleName();

  public UpdatedDeploymentEvent(Deployment deployment) {
    super(deployment);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getType() {
    return UpdatedDeploymentEvent.TYPE;
  }
  
}
