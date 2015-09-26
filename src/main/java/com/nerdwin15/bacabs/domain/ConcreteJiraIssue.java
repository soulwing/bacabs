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
package com.nerdwin15.bacabs.domain;

import com.nerdwin15.bacabs.JiraIssue;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Implementation of a {@link JiraIssue}.
 *
 * @author Christopher M. Dunavant
 */
@XmlRootElement(name = "jiraIssue")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConcreteJiraIssue implements JiraIssue {

  @XmlElement
  private String summary;
  
  @XmlElement
  private String description;

  @XmlElement
  private String status;

  @XmlElement
  private Integer subtaskCount;

  @XmlElement
  private Integer percentCompleted;
  
  @XmlElement
  private String acceptanceTaskStatus;
  
  @Override
  public String getSummary() {
    return summary;
  }
  
  /**
   * Sets the {@code title} property.
   * @param summary the value to set
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }
  
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

  @Override
  public String getStatus() {
    return status;
  }
  
  /**
   * Sets the {@code status} property.
   * @param status the value to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public Integer getSubtaskCount() {
    return subtaskCount;
  }

  /**
   * Sets the {@code subtaskCount} property.
   * @param subtaskCount the value to set
   */
  public void setSubtaskCount(Integer subtaskCount) {
    this.subtaskCount = subtaskCount;
  }
 
  @Override
  public Integer getPercentCompleted() {
    return percentCompleted;
  }

  /**
   * Sets the {@code percentCompleted} property.
   * @param percentCompleted the value to set
   */
  public void setPercentCompleted(Integer percentCompleted) {
    this.percentCompleted = percentCompleted;
  }
  
  @Override
  public String getAcceptanceTaskStatus() {
    return acceptanceTaskStatus;
  }
  
  /**
   * Sets the {@code acceptanceTaskStatus} property.
   * @param acceptanceTaskStatus the value to set
   */
  public void setAcceptanceTaskStatus(String acceptanceTaskStatus) {
    this.acceptanceTaskStatus = acceptanceTaskStatus;
  }
}
