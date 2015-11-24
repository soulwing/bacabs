package io.mikesir87.bacabs.web.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Definition of the JAXRS {@link Application} to remove the need of having it
 * in the web.xml
 *
 * @author Michael Irwin
 */
@ApplicationPath("/api")
public class BacabsApplication extends Application {
  // Nothing to do here, as we really only care about the @ApplicationPath
}
