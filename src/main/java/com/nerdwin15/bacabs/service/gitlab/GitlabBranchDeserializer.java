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
package com.nerdwin15.bacabs.service.gitlab;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.nerdwin15.bacabs.ConcreteGitlabBranch;
import com.nerdwin15.bacabs.GitlabBranch;

/**
 * Implemention of {@link JsonDeserializer} for a {@link GitlabBranch}
 *
 * @author Christopher M. Dunavant
 */
public class GitlabBranchDeserializer extends JsonDeserializer<GitlabBranch> {

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabBranch deserialize(JsonParser jp, DeserializationContext ctx)
      throws IOException, JsonProcessingException {
    JsonNode rootNode = jp.getCodec().readTree(jp);
    JsonNode commitNode = rootNode.path("commit");
    
    // There is no branch if there is no commit node.
    if (commitNode == null) {
      //System.out.println("No branch found");
      return null;
    }
    
    String committerName = commitNode.get("committer_name").asText();
    String commitDate = commitNode.get("committed_date").asText();
    
    ConcreteGitlabBranch branch = new ConcreteGitlabBranch();
    branch.setCommitterName(committerName);
    branch.setCommitDate(commitDate);
    
    return branch;
  }

}
