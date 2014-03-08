package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.OutgoingMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dteo on 7/03/2014.
 */
public class Channel {
  private String channel;

  public String getChannel() {
    return channel;
  }

  private List<String> users;

  public List<String> getUsers() {
    return users;
  }

  private Map<String, MessageHandler> handlers = new HashMap<>();

  public Channel(IRCClient client, String channel) {
    System.out.println("Joining Channel " + channel);

    client.send(CommandType.JOIN, channel);

    client.send(
      new OutgoingMessage(CommandType.WHO, channel)
        .listensTo(message -> {
          return message.command() == CommandType.WHO
        }).whenReplyReceived(messages -> {
        users.clear();

        for (Message message : messages) {

        }
      })
    );
  }

  public void bindAll(MessageHandler handler) {
    this.handlers.put(null, handler);
  }
}
