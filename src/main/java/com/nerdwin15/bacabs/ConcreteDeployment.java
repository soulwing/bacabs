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
package com.nerdwin15.bacabs;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An implementation of the {@link Deployment} object
 *
 * @author Michael Irwin
 */
@XmlRootElement(name = "deployment")
public class ConcreteDeployment implements Deployment {
  
  private static final String TO_STRING_FORMAT = "%s (%s) - %s";

  @XmlElement
  private String identifier;
  
  @XmlElement
  private String href;
  
  @XmlElement
  private String summary;
  
  @XmlElement
  private ConcreteJiraIssue jiraIssue;

  @XmlElement
  private ConcreteGitBranch gitlabBranch;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getIdentifier() {
    return identifier;
  }
  
  /**
   * Sets the {@code identifier} property.
   * @param identifier the value to set
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getHref() {
    return href;
  }
  
  /**
   * Sets the {@code href} property.
   * @param href the value to set
   */
  public void setHref(String href) {
    this.href = href;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getSummary() {
    return summary;
  }
  
  /**
   * Sets the {@code summary} property.
   * @param summary the value to set
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConcreteJiraIssue getJiraIssue() {
    return jiraIssue;
  }
  
  public void setJiraIssue(ConcreteJiraIssue jiraIssue) {
    this.jiraIssue = jiraIssue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitBranch getGitBranch() {
    return gitlabBranch;
  }

  public void setGitlabBranch(ConcreteGitBranch gitlabBranch) {
    this.gitlabBranch = gitlabBranch;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(identifier);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ConcreteDeployment)) return false;
    ConcreteDeployment that = (ConcreteDeployment) obj;
    if (this.identifier == null || that.getIdentifier() == null) return false;
    return this.identifier.equals(that.getIdentifier());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT, identifier, href, summary);
  }
}
