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
package com.nerdwin15.bacabs.service.jira;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.nerdwin15.bacabs.ConcreteJiraIssue;
import com.nerdwin15.bacabs.JiraIssue;

/**
 * Implemention of {@link JsonDeserializer} for a {@link JiraIssue}
 *
 * @author Christopher M. Dunavant
 */
public class JiraIssueDeserializer extends JsonDeserializer<JiraIssue> {

  private static final String USER_ACCEPTANCE_TEST = "User Acceptance Test";

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraIssue deserialize(JsonParser jp, DeserializationContext ctx)
      throws IOException, JsonProcessingException {
    
    JsonNode rootNode = jp.getCodec().readTree(jp);
    JsonNode fieldsNode = rootNode.path("fields");
    
    String summary = fieldsNode.get("summary").asText();
    String description = fieldsNode.get("description").asText();
    Integer statusId = fieldsNode.get("status").get("id").asInt();
    String statusName = fieldsNode.get("status").get("name").asText();
    Integer numTasks = fieldsNode.path("subtasks").size();
    Integer progressPercentage = fieldsNode.get("aggregateprogress").get("percent").asInt();
    Integer acceptanceTaskStatusId = null;
    String acceptanceTaskStatusName = null;
    for (JsonNode node : fieldsNode.path("subtasks")) {
      String taskSummary = node.get("fields").get("summary").asText();
      if (taskSummary.equals(USER_ACCEPTANCE_TEST)) {
        acceptanceTaskStatusId = node.get("fields").get("status").get("id").asInt();
        acceptanceTaskStatusName = node.get("fields").get("status").get("name").asText();
      }
    }
    
    ConcreteJiraIssue issue = new ConcreteJiraIssue();
    issue.setSummary(summary);
    issue.setDescription(description);
    issue.setStatusId(statusId);
    issue.setStatusName(statusName);
    issue.setNumTasks(numTasks);
    issue.setProgressPercentage(progressPercentage);
    issue.setAcceptanceTaskStatusId(acceptanceTaskStatusId);
    issue.setAcceptanceTaskStatusName(acceptanceTaskStatusName);
    
    return issue;
  }

}
