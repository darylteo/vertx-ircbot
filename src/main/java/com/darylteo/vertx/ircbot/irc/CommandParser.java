package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.buffer.Buffer;

/**
 * Created by dteo on 6/03/2014.
 */
public class CommandParser {
  private BufferTokenizer tokenizer = new BufferTokenizer();

  public CommandParser() {
  }

  public void append(Buffer buffer) {
    this.tokenizer.append(buffer);
  }

  public boolean hasNext() {
    return this.tokenizer.hasNext();
  }

  public Command next() {
    String line = tokenizer.next();

    if (line.startsWith("PING")) {
      return new Command(Commands.PING, "", line);
    }

    return new Command(Commands.UNKNOWN, "", line);
  }
}
