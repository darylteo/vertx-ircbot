package com.darylteo.vertx.ircbot.bot.plugins;

import com.darylteo.vertx.ircbot.bot.channels.Command;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.outgoing.PrivMsg;

/**
 * Created by dteo on 13/03/2014.
 */
public class CommandListPlugin extends Plugin {
  public CommandListPlugin() {
    super("help", "?");
  }

  @Override
  public void handle(IRCClient client, Command command) {
    System.out.println(client);
    System.out.println(command);
    System.out.println(command.getChannel());
    client.send(new PrivMsg(command.getChannel().getChannelName(), "Hello " + command.getUser().getNick()));
  }
}