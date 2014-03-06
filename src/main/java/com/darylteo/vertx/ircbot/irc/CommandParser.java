package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.buffer.Buffer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dteo on 6/03/2014.
 */
public class CommandParser {
  private BufferTokenizer tokenizer = new BufferTokenizer();

  private static Pattern MESSAGE_PATTERN = Pattern.compile("^(:(?<prefix>[^\\s]+) )?(?<command>[^\\s]+) (?<parameters>.+)$");

  public CommandParser() {
    System.out.println("PInG: " + MESSAGE_PATTERN.matcher(":barjavel.freenode.net 372 vertxbot :-").matches());

  }

  public void append(Buffer buffer) {
    this.tokenizer.append(buffer);
  }

  public boolean hasNext() {
    return this.tokenizer.hasNext();
  }

  public Command next() {
    String line = tokenizer.next();

    Matcher matcher = MESSAGE_PATTERN.matcher(line);
    System.out.println(line);
    System.out.println(matcher.matches());
    if (matcher.matches()) {
      return new Command(matcher.group("command"), matcher.group("parameters"), line);
    }

    return new Command("Unknown", "", line);
  }
}
