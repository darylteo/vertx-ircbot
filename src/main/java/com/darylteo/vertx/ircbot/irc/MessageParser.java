package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import org.vertx.java.core.buffer.Buffer;

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
    return new Message(tokenizer.next());
  }
}
