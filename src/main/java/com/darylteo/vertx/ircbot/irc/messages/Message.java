package com.darylteo.vertx.ircbot.irc.messages;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dteo on 7/03/2014.
 */
public class Message {
  private final String line;

  public String line() {
    return this.line;
  }

  private Prefix prefix;

  public Prefix getPrefix() {
    return prefix;
  }

  private String command;

  public String command() {
    return this.command;
  }

  private String[] parameters;

  public String[] parameters() {
    return this.parameters;
  }

  public Message(String line) {
    this.line = line;

    List<String> tokens = tokenize(line);
  }

  private void parse(List<String> tokens) {
    for (int i = 0; i < tokens.size(); i++) {
      String current = tokens.get(i);

      if (i == 0 && current.startsWith(":")) {
        this.prefix = new Prefix(current);
      } else if (this.command == null) {
        this.command = current;
      } else {
        this.parameters = tokens.subList(i, tokens.size()).toArray(new String[0]);
        break;
      }
    }
  }

  private List<String> tokenize(String line) {
    List<String> tokens = new LinkedList<>();

    while (line.length() > 0) {
      if (line.startsWith(":") && !tokens.isEmpty()) {
        // eat everything else, final token
        tokens.add(line);
      } else {
        int space = line.indexOf(" ");
        tokens.add(line.substring(0, space));
        line = line.substring(space + 1);
      }
    }

    return tokens;
  }

  @Override
  public String toString() {
    return "[" + this.command + "]: " + line;
  }
}
