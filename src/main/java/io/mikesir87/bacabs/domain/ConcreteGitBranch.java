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
package io.mikesir87.bacabs.domain;

import io.mikesir87.bacabs.GitBranch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Implementation of {@link GitBranch} that is ready for JAXB/Jackson
 * marshalling.
 *
 * @author Christopher M. Dunavant
 * @author Michael Irwin
 */
@XmlRootElement(name = "branch")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConcreteGitBranch implements GitBranch {

  @XmlElement
  private String name;

  @XmlElement
  private String lastCommitAuthor;
  
  @XmlElement
  private String lastCommitDate;

  @Override
  public String getName() {
    return name;
  }

  /**
   * Set the name of the branch
   * @param name The name of the branch
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getLastCommitAuthor() {
    return lastCommitAuthor;
  }

  /**
   * Sets the {@code lastCommitAuthor} property.
   * @param lastCommitAuthor the value to set
   */
  public void setLastCommitAuthor(String lastCommitAuthor) {
    this.lastCommitAuthor = lastCommitAuthor;
  }
  
  @Override
  public String getLastCommitDate() {
    return lastCommitDate;
  }

  /**
   * Sets the {@code lastCommitDate} property.
   * @param lastCommitDate the value to set
   */
  public void setLastCommitDate(String lastCommitDate) {
    this.lastCommitDate = lastCommitDate;
  }
}
