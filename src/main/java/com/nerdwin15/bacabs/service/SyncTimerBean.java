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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * A timer that is used to trigger the sync job.
 *
 * @author Michael Irwin
 */
@Startup
@Singleton
public class SyncTimerBean {

  private static final Logger logger = LoggerFactory.getLogger(SyncTimerBean.class);
  
  @Inject
  protected DeploymentSyncService syncService;
  
  @Schedule(second="*/10", hour="*", minute="*", month="*", persistent=false)
  public void doSync() {
    logger.info("Triggering a sync");
    try {
      syncService.performDeploymentSync();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
}
