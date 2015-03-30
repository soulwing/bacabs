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
package com.nerdwin15.bacabs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nerdwin15.bacabs.service.jira.JiraIssueDeserializer;

/**
 * Implementation of a {@link JiraIssue}.
 *
 * @author Christopher M. Dunavant
 */
@XmlRootElement(name = "jiraIssue")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonDeserialize(using = JiraIssueDeserializer.class)
public class ConcreteJiraIssue implements JiraIssue {

  @XmlElement
  private String summary;
  
  @XmlElement
  private String description;

  @XmlElement
  private Integer statusId;

  @XmlElement
  private String statusName;

  @XmlElement
  private Integer numTasks;

  @XmlElement
  private Integer progressPercentage;
  
  @XmlElement
  private Integer acceptanceTaskStatusId;

  @XmlElement
  private String acceptanceTaskStatusName;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getSummary() {
    return summary;
  }
  
  /**
   * Sets the {@code title} property.
   * @param title the value to set
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return description;
  }
  
  /**
   * Sets the {@code description} property.
   * @param description the value to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getStatusId() {
    return statusId;
  }
  
  /**
   * Sets the {@code statusId} property.
   * @param statusId the value to set
   */
  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatusName() {
    return statusName;
  }
  
  /**
   * Sets the {@code statusName} property.
   * @param statusName the value to set
   */
  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getNumTasks() {
    return numTasks;
  }

  /**
   * Sets the {@code numTasks} property.
   * @param numTasks the value to set
   */
  public void setNumTasks(Integer numTasks) {
    this.numTasks = numTasks;
  }
 
  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getProgressPercentage() {
    return progressPercentage;
  }

  /**
   * Sets the {@code progressPercentage} property.
   * @param progressPercentage the value to set
   */
  public void setProgressPercentage(Integer progressPercentage) {
    this.progressPercentage = progressPercentage;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getAcceptanceTaskStatusId() {
    return acceptanceTaskStatusId;
  }

  /**
   * Sets the {@code acceptanceTaskStatusId} property.
   * @param acceptanceTaskStatusId the value to set
   */
  public void setAcceptanceTaskStatusId(Integer acceptanceTaskStatusId) {
    this.acceptanceTaskStatusId = acceptanceTaskStatusId;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getAcceptanceTaskStatusName() {
    return acceptanceTaskStatusName;
  }
  
  /**
   * Sets the {@code acceptanceTaskStatusName} property.
   * @param acceptanceTaskStatusName the value to set
   */
  public void setAcceptanceTaskStatusName(String acceptanceTaskStatusName) {
    this.acceptanceTaskStatusName = acceptanceTaskStatusName;
  }
}
