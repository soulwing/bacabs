package io.mikesir87.bacabs.service.jira.client;

import io.mikesir87.bacabs.JiraIssue;

import javax.ws.rs.core.Response;

/**
 * A simple converter that deserializes the {@link Response} into a
 * {@link JiraIssue}.
 */
public interface ResponseHandler {

  /**
   * Convert the provided Response to a JiraIssue
   * @param response A JAXRS Response
   * @return A JiraIssue if an issue is in the response. Otherwise, null.
   */
  JiraIssue convertResponseToIssue(Response response);
}
