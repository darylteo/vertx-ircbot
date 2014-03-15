package com.darylteo.vertx.ircbot.irc.messages.outgoing;

import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.messages.OutgoingMessage;

/**
 * Created by dteo on 16/03/2014.
 */
public class PrivMsg extends OutgoingMessage {
  public PrivMsg(String target, String message) {
    super(CommandType.PRIVMSG, true, target, message);
  }
}
