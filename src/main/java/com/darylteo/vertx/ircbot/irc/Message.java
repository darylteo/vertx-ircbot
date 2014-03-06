package com.darylteo.vertx.ircbot.irc;

/**
 * Created by dteo on 7/03/2014.
 */
public class Message {
  private String line;

  public String line() {
    return this.line;
  }

  private String command;

  public String command() {
    return this.command;
  }

  private String parameters;

  public String parameters() {
    return this.parameters;
  }

  public Message(String command, String parameters, String line) {
    this.command = command;
    this.parameters = parameters;
    this.line = line;
  }

  @Override
  public String toString() {
    return "[" + this.command + "]: " + line;
  }
}
