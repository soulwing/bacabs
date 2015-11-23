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
  @Property
  protected String verifyPath;

  @Inject
  @Property
  protected Integer expectedResponse;

  @Override
  public Deployment.Status getStatus(Deployment deployment) {
    String verifyUrl = deployment.getHref() + verifyPath;

    try {
      URL url = new URL(verifyUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      int code = connection.getResponseCode();
      connection.disconnect();
      return (expectedResponse.equals(code)) ?
          Deployment.Status.VERIFIED : Deployment.Status.UNKNOWN;
    } catch (IOException e) {
      return Deployment.Status.UNKNOWN;
    }
  }
}
