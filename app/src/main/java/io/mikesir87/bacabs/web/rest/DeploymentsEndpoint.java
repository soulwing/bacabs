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
package io.mikesir87.bacabs.web.rest;

import io.mikesir87.bacabs.service.DeploymentService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * An endpoint used for interacting with deployments
 *
 * @author Michael Irwin
 */
@Path("/deployments")
public class DeploymentsEndpoint {

  @Inject
  protected DeploymentService deploymentService;

  @GET
  public Response getDeployments() throws Exception {
    return Response.ok(deploymentService.getDeployments()).build();
  }

}
