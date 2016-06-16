package io.mikesir87.bacabs.event.listener;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;
import io.mikesir87.bacabs.event.ClientEvent;
import io.mikesir87.bacabs.event.DeploymentStatusChangeEvent;
import org.soulwing.cdi.properties.Property;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by mikesir on 6/15/16.
 */
@Startup
@Singleton
public class SlackListener {

  private static final String MESSAGE_TMPL = "Deployment %s is now %s (<%s|open>)";

  @Inject @Property(name = "slack.hookUrl")
  protected String slackHookUrl;

  @Inject @Property(name = "slack.channel")
  protected String channel;

  @Inject @Property(name = "slack.icon")
  protected String icon;

  public void onEvent(@Observes DeploymentStatusChangeEvent statusChange) {
    if (slackHookUrl.equals("none"))
      return;

    String message = String.format(MESSAGE_TMPL,
        statusChange.getDeployment().getIdentifier(),
        statusChange.getDeployment().getStatus().getDisplayName(),
        statusChange.getDeployment().getHref());

    try {
      new Slack(slackHookUrl)
          .sendToChannel(channel)
          .icon(icon)
          .push(new SlackMessage(message));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
