package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.buffer.Buffer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dteo on 6/03/2014.
 */
public class MessageParser {
  private BufferTokenizer tokenizer = new BufferTokenizer();

  private static Pattern MESSAGE_PATTERN = Pattern.compile("^(:(?<prefix>[^\\s]+) )?(?<command>[^\\s]+) (?<parameters>.+)$");

  public MessageParser() {
  }

  public void append(Buffer buffer) {
    this.tokenizer.append(buffer);
  }

  public boolean hasNext() {
    return this.tokenizer.hasNext();
  }

  public Message next() {
    String line = tokenizer.next();

    Matcher matcher = MESSAGE_PATTERN.matcher(line);
    if (matcher.matches()) {
      return new Message(matcher.group("command"), matcher.group("parameters"), line);
    }

    return new Message("Unknown", "", line);
  }
}
