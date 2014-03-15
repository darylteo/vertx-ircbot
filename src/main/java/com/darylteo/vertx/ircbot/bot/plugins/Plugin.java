package com.darylteo.vertx.ircbot.bot.plugins;

import com.darylteo.vertx.ircbot.bot.channels.Channel;
import com.darylteo.vertx.ircbot.bot.channels.Command;
import com.darylteo.vertx.ircbot.irc.IRCClient;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dteo on 13/03/2014.
 */
public abstract class Plugin {
  private Set<String> accepts = new HashSet<String>();

  public Plugin(String... keywords) {
    for (String keyword : keywords) {
      this.accepts.add(keyword.toLowerCase());
    }
  }

  public boolean respondsTo(Command command) {
    return accepts.contains(command.getKeyword().toLowerCase());
  }

  public abstract void handle(IRCClient client, Command command);
}
