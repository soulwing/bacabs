/*
 * File created on Mar 31, 2015
 *
 * Copyright (c) 2015 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mikesir87.bacabs.service.git;import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

/**
 * A {@link org.eclipse.jgit.transport.SshSessionFactory} that uses a specified
 * private key as an identity for SSH public-key authentication.
 *
 * @author Carl Harris
 */
class CustomConfigSessionFactory extends JschConfigSessionFactory {

  private final URL privateKeyLocation;

  public CustomConfigSessionFactory(URL privateKeyLocation) {
    this.privateKeyLocation = privateKeyLocation;
  }

  @Override
  protected void configure(OpenSshConfig.Host host, Session session) {
    session.setConfig("StrictHostKeyChecking", "yes");
  }

  @Override
  protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
    JSch jsch = super.getJSch(hc, fs);
    jsch.removeAllIdentity();
    try {
      jsch.addIdentity(new File(privateKeyLocation.toURI()).getAbsolutePath());
    }
    catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
    return jsch;
  }

}