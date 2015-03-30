/*
 * File created on Mar 21, 2015 
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
import com.nerdwin15.bacabs.service.gitlab.GitlabBranchDeserializer;

/**
 * Implementation of {@link GitlabBranch}.
 *
 * @author Christopher M. Dunavant
 */
@XmlRootElement(name = "gitlabBranch")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonDeserialize(using = GitlabBranchDeserializer.class)
public class ConcreteGitlabBranch implements GitlabBranch {

  @XmlElement
  private String committerName;
  
  @XmlElement
  private String commitDate;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommitterName() {
    return committerName;
  }

  /**
   * Sets the {@code committerName} property.
   * @param name the value to set
   */
  public void setCommitterName(String committerName) {
    this.committerName = committerName;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommitDate() {
    return commitDate;
  }

  /**
   * Sets the {@code commitDate} property.
   * @param date the value to set
   */
  public void setCommitDate(String commitDate) {
    this.commitDate = commitDate;
  }
}
