package com.darylteo.vertx.ircbot.bot.plugins;

import com.darylteo.vertx.ircbot.bot.channels.Channel;
import com.darylteo.vertx.ircbot.bot.channels.Command;

/**
 * Created by dteo on 13/03/2014.
 */
public class CommandListPlugin extends Plugin {
  public CommandListPlugin() {
    super("help", "?");
  }

  @Override
  public void handle(Channel channel, Command command) {
    channel.send("Hello " + command.getUser());
  }
}
