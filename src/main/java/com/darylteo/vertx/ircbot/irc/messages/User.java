package com.darylteo.vertx.ircbot.irc.messages;

/**
 * Created by dteo on 12/03/2014.
 */
public class User implements Comparable<User> {
  private String username;
  private String prefix;
  private String nick;
  private String host;

  public String getUsername() {
    return username;
  }

  public String getNick() {
    return nick;
  }

  public String getHost() {
    return host;
  }

  public User(String userString) {
    StringBuilder builder = new StringBuilder();
    String username = null;
    String nick = null;
    String host = null;

    for (int i = 1; i < userString.length(); i++) {
      char c = userString.charAt(i);

      if (c == '!') {
        username = builder.toString();
        builder.setLength(0);
      } else if (c == '@') {
        nick = builder.toString();
        builder.setLength(0);
      } else {
        builder.append(c);
      }
    }

    host = builder.toString();
    init(username, nick, host);
  }

  public User(String username, String nick, String host) {
    init(username, nick, host);
  }

  private void init(String username, String nick, String host) {
    this.username = username;
    this.nick = nick;
    this.host = host;

    if (!Character.isLetterOrDigit(this.nick.charAt(0))) {
      this.prefix = this.nick.charAt(0) + "";
      this.nick = this.nick.substring(1);
    }
  }

  public String toString() {
    return this.getNick();
  }

  @Override
  public int compareTo(User other) {
    return this.nick.compareTo(other.nick);
  }
}
