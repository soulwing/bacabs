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
package io.mikesir87.bacabs.service;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.cdi.properties.Property;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
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

  @Resource
  protected TimerService timerService;

  @Inject
  @Property(name = "sync.schedule")
  protected String schedule;
  
  @Inject
  protected DeploymentSyncService syncService;

  @PostConstruct
  public void init() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setPersistent(false);
    timerService.createCalendarTimer(newSchedule(schedule), timerConfig);
  }

  @Timeout
  public void doSync(Timer timer) {
    logger.info("Triggering a sync");
    try {
      syncService.performDeploymentSync();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ScheduleExpression newSchedule(String expression) {
    Validate.notNull(expression, "expression is required");
    String[] tokens = expression.split("\\s+");
    Validate.isTrue(tokens.length >= 6 && tokens.length <= 7,
        "expression must include 6 or 7 space-delimited tokens; got '"
            + expression + "'");
    ScheduleExpression se = new ScheduleExpression();
    se.second(tokens[0]);
    se.minute(tokens[1]);
    se.hour(tokens[2]);
    se.dayOfMonth(tokens[3]);
    se.month(tokens[4]);
    se.dayOfWeek(tokens[5]);
    if (tokens.length == 7) {
      se.year(tokens[6]);
    }
    return se;
  }
}
