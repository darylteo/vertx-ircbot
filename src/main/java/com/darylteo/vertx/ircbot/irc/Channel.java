package com.darylteo.vertx.ircbot.irc;

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

    client.send("JOIN", channel);
  }

  public void bindAll(MessageHandler handler) {
    this.handlers.put(null, handler);
  }
}
