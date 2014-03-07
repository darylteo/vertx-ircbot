package com.darylteo.vertx.ircbot.irc.messages;

/**
 * Created by dteo on 7/03/2014.
 */
public class Target {
  private String name;

  public String getName() {
    return name;
  }

  public boolean isChannel() {
    return this.name.startsWith("#");
  }
}
