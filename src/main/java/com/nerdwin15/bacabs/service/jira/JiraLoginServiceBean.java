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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.security.sasl.AuthenticationException;

/**
 * Implementation of {@link JiraLoginService}.
 *
 * @author Christopher M. Dunavant
 */
@ApplicationScoped
public class JiraLoginServiceBean implements JiraLoginService {

  @Inject @org.soulwing.cdi.properties.Property
  protected String jiraUsername;
  
  @Inject @org.soulwing.cdi.properties.Property
  protected String jiraPassword;

  @Inject @org.soulwing.cdi.properties.Property
  protected String jiraRestUrl;

  @Inject @org.soulwing.cdi.properties.Property
  protected String casRestUrl;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthenticatedRestUrl(String identifier) throws IOException {
    String ticketGrantingTicket = getTicketGrantingTicket(jiraUsername, 
        jiraPassword);
    String serviceGrantingTicket = getServiceGrantingTicket(ticketGrantingTicket, 
        jiraRestUrl + identifier);
    URL connectionUrl = new URL(jiraRestUrl + identifier + "?ticket=" + serviceGrantingTicket);

    HttpURLConnection conn = (HttpURLConnection) connectionUrl.openConnection();
    
    // We don't want to follow the redirect ... we want the url location 
    // with the jsessionid
    conn.setInstanceFollowRedirects(false);
    int code = conn.getResponseCode();

    String location = null;
    if (code == 302) {
      // This is the redirect with the jsessionid
      location = conn.getHeaderField("Location");
    } else {
      conn.disconnect();
      throw new IOException("Expected a 302 redirect!");
    }
    
    conn.disconnect();
    return location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServiceGrantingTicket(String tgtLocation, String serviceUrl)
      throws IOException {
    String params = "service=" + serviceUrl;

    HttpURLConnection conn = post(tgtLocation, params);
    StringBuilder responseBuilder = new StringBuilder();
    BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream(), "UTF-8"));
    String input;
    while ((input = in.readLine()) != null) {
      responseBuilder.append(input);
    }
    in.close();

    String response = responseBuilder.toString();
    //System.out.println("SGT -> " + response);
     
    return response;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTicketGrantingTicket(String username, String password)
      throws IOException {
    String params = "username=" + username;
    params += "&password=" + URLEncoder.encode(password, "UTF-8");
    
    HttpURLConnection conn = post(casRestUrl, params);
    
    if(conn.getResponseCode() == 400) {
      throw new AuthenticationException("bad username or password");
    }
    
    String location = conn.getHeaderField("Location");
    //System.out.println("TGT LOCATION -> " + location);
    
    return location;
  }
  
  public HttpsURLConnection post(String url, String urlParams) throws IOException {
      URL obj = new URL(url);
      HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
   
      //add request header
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Accept", "text/plain");
   
      // Send post request
      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.writeBytes(urlParams);
      wr.flush();
      wr.close();
      
      //System.out.println("\nSending 'POST' request to URL : " + url);
      //System.out.println("Post parameters : " + urlParams);
      //System.out.println("Response Code : " + conn.getResponseCode());
      
      return conn;
  }

}
