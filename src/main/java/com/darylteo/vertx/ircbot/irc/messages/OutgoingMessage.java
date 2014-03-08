package com.darylteo.vertx.ircbot.irc.messages;

import com.darylteo.vertx.ircbot.irc.CommandType;
import org.vertx.java.core.Handler;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by dteo on 8/03/2014.
 */
public class OutgoingMessage {
  private CommandType command;
  private String[] parameters;

  private Predicate<Message> predicate;

  private List<Message> replies = new LinkedList<>();
  private Handler<List<Message>> handler;

  public CommandType command() {
    return command;
  }

  public String[] parameters() {
    return parameters;
  }

  public OutgoingMessage listensTo(Predicate<Message> predicate) {
    this.predicate = predicate;
    return this;
  }

  public OutgoingMessage whenReplyReceived(Handler<List<Message>> messageHandler) {
    // TODO: Warn if adding reply handler without any predicate
    this.handler = messageHandler;
    return this;
  }

  public boolean isWaiting() {
    return this.predicate != null;
  }

  public OutgoingMessage(CommandType command, String... parameters) {
    this.command = command;
    this.parameters = parameters;
  }

  public void handle(Message message) {
    if (!isWaiting()) {
      return;
    }

    this.predicate.test(message);
  }
}
