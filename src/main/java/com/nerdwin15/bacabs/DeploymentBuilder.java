package com.nerdwin15.bacabs;

/**
 * A simple builder interface that is used to generate new {@link Deployment}
 * objects.  The builder should be designed so that after <code>build</code>
 * is invoked, the builder is reset for a new build.
 *
 * @author Michael Irwin
 */
public interface DeploymentBuilder {

  /**
   * Set the href for the new deployment
   * @param href The href
   * @return The builder
   */
  DeploymentBuilder href(String href);

  /**
   * Set the identifier for the new deployment
   * @param identifier The identifier
   * @return The builder
   */
  DeploymentBuilder identifier(String identifier);

  /**
   * Create the new Deployment.  After creation, the builder resets and
   * can be used for a new deployment.
   * @return
   */
  Deployment build();
}
