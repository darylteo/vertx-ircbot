package com.darylteo.vertx.ircbot.irc.messages;

import com.darylteo.vertx.ircbot.irc.CommandType;
import org.vertx.java.core.Handler;

import java.util.Arrays;
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

  public OutgoingMessage(CommandType command, boolean escapeLastParameter, String... parameters) {
    this.command = command;
    this.parameters = Arrays.copyOf(parameters, parameters.length);

    if (escapeLastParameter && this.parameters.length > 0) {
      this.parameters[this.parameters.length - 1] = ":" + this.parameters[this.parameters.length - 1];
    }
  }

  public CommandType command() {
    return command;
  }

  public String[] parameters() {
    return parameters;
  }

}
