package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;

/**
 * Created by dteo on 6/03/2014.
 */
@FunctionalInterface
public interface MessageHandler {
  public void handle(Message message);
}
