package com.darylteo.vertx.ircbot.irc.messages.incoming;

import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.User;

/**
 * Created by dteo on 12/03/2014.
 */
public class PrivMsg extends Message {
  private User sender;
  private String recipient;
  private String message;

  public PrivMsg(String prefix, String[] parameters) {
    super(prefix, CommandType.PRIVMSG, parameters);

    if (prefix != null) {
      this.sender = new User(prefix);
    }

    this.recipient = parameters[0];
    this.message = parameters[1];
  }

  public User getSender() {
    return sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getMessage() {
    return message;
  }


}
