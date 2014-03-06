package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.buffer.Buffer;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by dteo on 6/03/2014.
 */
public class BufferTokenizer {
  private Buffer currentBuffer = new Buffer();
  private Queue<String> commands = new LinkedList<>();

  public void append(Buffer buffer) {
    for (int i = 0; i < buffer.length(); i++) {
      byte b = buffer.getByte(i);

      if (b == '\n') {
        this.commands.add(new String(this.currentBuffer.getBytes(), StandardCharsets.UTF_8));
        this.currentBuffer = new Buffer();
      } else if (b == '\r') {
        // skip
      } else {
        this.currentBuffer.appendByte(b);
      }
    }
  }

  public boolean hasNext() {
    return !commands.isEmpty();
  }

  public String next() {
    return commands.poll();
  }
}
