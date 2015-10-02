package io.mikesir87.bacabs.service.jira.client;

import io.mikesir87.bacabs.JiraIssue;
import io.mikesir87.bacabs.JiraIssueBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;
import java.io.StringReader;

/**
 * A {@link ResponseHandler} that works with JSON data from the JIRA REST API.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class JsonResponseHandler implements ResponseHandler {

  private static final String USER_ACCEPTANCE_TEST = "user acceptance test";

  @Inject
  protected JiraIssueBuilder jiraIssueBuilder;

  @Override
  public JiraIssue convertResponseToIssue(Response response) {
    String responseData = response.readEntity(String.class);
    JsonReader reader = Json.createReader(new StringReader(responseData));
    JsonObject baseObject = reader.readObject();
    if (baseObject.containsKey("errorMessages") || response.getStatus() == 404
        || !baseObject.containsKey("fields"))
      return null;

    JsonObject fields = baseObject.getJsonObject("fields");
    return jiraIssueBuilder
        .summary(fields.getString("summary"))
        .description(fields.getString("description"))
        .status(fields.getJsonObject("status").getString("name"))
        .acceptanceTaskStatus(getAcceptanceTaskStatus(fields))
        .percentCompleted(getPercentCompleted(fields))
        .subtaskCount(fields.containsKey("subtasks") ? fields.getJsonArray("subtasks").size() : null)
        .build();
  }

  private String getAcceptanceTaskStatus(JsonObject fields) {
    if (!fields.containsKey("subtasks"))
      return null;

    JsonArray subtasks = fields.getJsonArray("subtasks");
    for (int i = 0; i < subtasks.size(); i++) {
      JsonObject subtask = subtasks.getJsonObject(i);
      JsonObject subtaskFields = subtask.getJsonObject("fields");
      if (subtaskFields.getString("summary").toLowerCase().equals(USER_ACCEPTANCE_TEST))
        return subtaskFields.getJsonObject("status").getString("name");
    }

    return null;
  }

  private Integer getPercentCompleted(JsonObject fields) {
    if (fields.containsKey("aggregateprogress")
        && fields.getJsonObject("aggregateprogress").containsKey("percent"))
      return fields.getJsonObject("aggregateprogress").getInt("percent");
    return null;
  }
}
