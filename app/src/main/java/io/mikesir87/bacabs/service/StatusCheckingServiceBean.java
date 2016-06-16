package io.mikesir87.bacabs.service;

import io.mikesir87.bacabs.Deployment;
import org.soulwing.cdi.properties.Property;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A CDI-injectable implementation of a {@link StatusCheckingService}.
 *
 * @author Michael Irwin
 */
@ApplicationScoped
public class StatusCheckingServiceBean implements StatusCheckingService {

  @Inject
  @Property(name = "statusCheck.url")
  protected String verifyPath;

  @Inject
  @Property(name = "statusCheck.expectedResponse")
  protected Integer expectedResponse;

  @Override
  public Deployment.Status getStatus(Deployment deployment) {
    String verifyUrl = deployment.getHref() + verifyPath;

    try {
      int code = getResponseCode(verifyUrl);
      return (expectedResponse.equals(code)) ?
          Deployment.Status.VERIFIED : Deployment.Status.UNKNOWN;
    } catch (IOException e) {
      return Deployment.Status.UNKNOWN;
    }
  }

  private int getResponseCode(String statusUrl) throws IOException {
    URL url = new URL(statusUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setInstanceFollowRedirects(true);
    connection.setRequestMethod("GET");
    connection.connect();
    int code = connection.getResponseCode();
    connection.disconnect();
    if (code / 100 == 3) {
      return getResponseCode(connection.getHeaderField("Location"));
    }
    return code;
  }
}
