package com.darylteo.vertx.ircbot.irc;

/**
 * Created by dteo on 6/03/2014.
 */
@FunctionalInterface
public interface MessageHandler {
  public void handle(String command);
}
