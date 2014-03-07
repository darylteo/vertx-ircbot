package com.darylteo.vertx.ircbot.irc.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dteo on 7/03/2014.
 */
public class Prefix {
  public static Pattern PREFIX_PATTERN = Pattern.compile("(?<name>.+?)(!(?<user>.+?)@(?<host>.+))?");

  private String prefix;

  private String name;

  private String user;
  private String host;

  public String getName() {
    return name;
  }

  public String getUser() {
    return user;
  }

  public String getHost() {
    return host;
  }

  public boolean isServerPrefix() {
    return this.host == null;
  }

  public Prefix(String prefix) {
    this.prefix = prefix;

    Matcher matcher = PREFIX_PATTERN.matcher(prefix);

    if (matcher.matches()) {
      this.name = matcher.group("name");
      this.user = matcher.group("user");
      this.host = matcher.group("host");
    } else {
      // TODO: Exception handling
    }
  }

  @Override
  public String toString() {
    return this.prefix;
  }
}
