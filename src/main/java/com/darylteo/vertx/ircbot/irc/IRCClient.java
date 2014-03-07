package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.net.NetSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;
  private MessageParser parser = new MessageParser();

  private Map<String, MessageHandler> handlers = new HashMap<>();

  public IRCClient(NetSocket socket) {
    this.socket = socket;

    this.bind("PING", message -> {
      this.send("PONG", message.parameters());
    });

    socket.dataHandler(buffer -> {
      this.parser.append(buffer);

      while (this.parser.hasNext()) {
        this.handle(this.parser.next());
      }
    });
  }

  //
  // Commands
  public void ident(String nick, String real) {
    this.send("NICK", nick);
    this.send("USER", nick, "0", "*", ":" + real);
  }

  public void join(String channel) {
    if (!channel.startsWith("#")) {
      channel = "#" + channel;
    }
    this.send("JOIN", channel);
  }

  public void quit(String reason) {
    send("QUIT " + reason);
  }

  //
  // Handler Methods
  public void bind(String command, MessageHandler handler) {
    this.handlers.put(command.toUpperCase(), handler);
  }

  private void handle(Message message) {
    System.out.println(message.line());
    if (this.handlers.containsKey(message.command())) {
      this.handlers.get(message.command()).handle(message);
    }
  }

  //
  // Sending Methods
  public void send(String command, String... parameters) {
    send(String.format("%s %s", command, String.join(" ", parameters)));
  }

  private void send(String message) {
    System.out.println(" -- " + message);
    this.socket.write(message);
    this.socket.write("\n");
  }
}