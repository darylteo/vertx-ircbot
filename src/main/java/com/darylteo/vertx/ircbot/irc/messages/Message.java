package com.darylteo.vertx.ircbot.irc.messages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    parse(tokenize(line));
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
      // EXIT: detect trailing parameter, break if found.
      if (line.startsWith(":") && !tokens.isEmpty()) {
        // eat everything else, final token
        tokens.add(line);
        break;
      }

      // find next parameter
      int space = line.indexOf(" ");

      // EXIT: detect final parameter (without spaces)
      if (space == -1) {
        tokens.add(line);
        break;
      }

      tokens.add(line.substring(0, space));
      line = line.substring(space + 1);
    }

    return tokens;
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
