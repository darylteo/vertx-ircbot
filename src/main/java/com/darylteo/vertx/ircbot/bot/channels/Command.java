package com.darylteo.vertx.ircbot.bot.channels;

import com.darylteo.vertx.ircbot.irc.messages.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dteo on 13/03/2014.
 */
public class Command {
  private Channel channel;
  private User user;

  private String keyword;
  private List<String> args;

  public Command(String[] parts, Channel channel, User user) {
    this.keyword = parts[0];
    this.channel = channel;
    this.args = parts.length > 1 ? Arrays.asList(parts).subList(1, parts.length) : new ArrayList<>();
    this.user = user;
  }

  public Channel getChannel() {
    return channel;
  }

  public User getUser() {
    return user;
  }

  public String getKeyword() {
    return keyword;
  }

  public List<String> getArgs() {
    return args;
  }
}
