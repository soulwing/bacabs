package com.nerdwin15.bacabs.domain;

import com.nerdwin15.bacabs.Deployment;
import com.nerdwin15.bacabs.DeploymentBuilder;

import javax.enterprise.context.Dependent;

/**
 * A {@link DeploymentBuilder} that builds new {@link ConcreteDeployment}
 * objects.
 *
 * @author Michael Irwin
 */
@Dependent
public class ConcreteDeploymentBuilder implements DeploymentBuilder {

  private ConcreteDeployment delegate = new ConcreteDeployment();

  @Override
  public DeploymentBuilder href(String href) {
    delegate.setHref(href);
    return this;
  }

  @Override
  public DeploymentBuilder identifier(String identifier) {
    delegate.setIdentifier(identifier);
    return this;
  }

  @Override
  public Deployment build() {
    Deployment returningDeployment = delegate;
    delegate = new ConcreteDeployment();
    return returningDeployment;
  }
}
