package com.darylteo.vertx.ircbot.irc.messages;

import com.darylteo.vertx.ircbot.irc.CommandType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by dteo on 7/03/2014.
 */
public class Message {
  private String prefix;

  public String getPrefix() {
    return prefix;
  }

  private CommandType command;

  public CommandType command() {
    return this.command;
  }

  public boolean isCommand(CommandType... types) {
    return Arrays.asList(types).contains(this.command);
  }

  private String[] parameters;

  public String[] parameters() {
    return this.parameters;
  }

  public String parameters(int index) {
    return this.parameters[index];
  }

  public Message(String prefix, CommandType command, String[] parameters) {
    this.prefix = prefix;
    this.command = command;
    this.parameters = parameters == null ? new String[0] : parameters;
  }

  @Override
  public String toString() {
    String params = Arrays.asList(this.parameters)
      .stream()
      .map(p -> String.format("[%s]", p))
      .collect(Collectors.joining());

    return String.format("[%s][%s]: %s", this.prefix, this.command, params);
  }
}
