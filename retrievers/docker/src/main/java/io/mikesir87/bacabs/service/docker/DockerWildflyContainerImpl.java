package io.mikesir87.bacabs.service.docker;

/**
 * A simple non-mutable POJO that implements the {@link DockerWildflyContainer}.
 *
 * @author Michael Irwin
 */
public class DockerWildflyContainerImpl implements DockerWildflyContainer {

  private final String href;
  private final String identifier;

  /**
   * Construct a new instance
   * @param href The href for the container
   * @param identifier The href for the identifier
   */
  public DockerWildflyContainerImpl(String href, String identifier) {
    this.href = href;
    this.identifier = identifier;
  }

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public String getIdentifier() {
    return identifier;
  }
}
