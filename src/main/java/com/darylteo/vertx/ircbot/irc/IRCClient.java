package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.OutgoingMessage;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.core.net.NetSocket;

import java.util.*;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;
  private MessageParser parser = new MessageParser();

  private Map<CommandType, MessageHandler> handlers = new HashMap<>();
  private Map<String, Channel> channels = new HashMap<>();
  private List<OutgoingMessage> awaitingReplies = new LinkedList<OutgoingMessage>();

  //
  // Constructors
  public IRCClient(Vertx vertx, String nick, String name, Handler<IRCClient> handler) {
    NetClient client = vertx.createNetClient();

    client.connect(8000, "irc.freenode.org", result -> {
      if (result.succeeded()) {
        System.out.println("Succeeded in connecting to server");

        this.init(result.result());
        this.ident(nick, name);

        handler.handle(this);
      } else {
        System.out.println("Connection to irc server failed");
      }
    });
  }

  //
  // Setup
  private void init(NetSocket socket) {
    this.socket = socket;

    // Respond to Pings
    this.bind(CommandType.PING, message ->
      this.send(CommandType.PONG, message.parameters())
    );

    // pass other messages to channels/subscribers
    socket.dataHandler(buffer -> this.handle(buffer));
  }

  //
  // CommandType
  public void ident(String nick, String real) {
    this.send(CommandType.NICK, nick);
    this.send(CommandType.USER, nick, "0", "*", ":" + real);
  }

  public Channel join(String channel) {
    if (!channel.startsWith("#")) {
      channel = "#" + channel;
    }

    Channel result = new Channel(this, channel);
    this.channels.put(channel, result);
    return result;
  }

  public void quit(String reason) {
    send("QUIT " + reason);
  }

  //
  // Handler Methods
  public void bind(String command, MessageHandler handler) {
    this.bind(CommandType.valueOf(command.toUpperCase()), handler);
  }

  public void bind(CommandType command, MessageHandler handler) {
    this.handlers.put(command, handler);
  }

  private void handle(Buffer buffer) {
    this.parser.append(buffer);

    while (this.parser.hasNext()) {
      this.handle(this.parser.next());
    }
  }

  private void handle(Message message) {
    System.out.println(message);
    if (this.handlers.containsKey(message.command())) {
      this.handlers.get(message.command()).handle(message);
    }

    Iterator<OutgoingMessage> iter = this.awaitingReplies.iterator();

    while (iter.hasNext()) {
      OutgoingMessage awaiting = iter.next();

      awaiting.handle(message);
      if (!awaiting.isWaiting()) {
        iter.remove();
      }
    }
  }

  //
  // Sending Methods
  public void send(CommandType command, String... parameters) {
    this.send(String.format("%s %s", command.code(), String.join(" ", parameters)));
  }

  public void send(String command, String... parameters) {
    this.send(String.format("%s %s", command, String.join(" ", parameters)));
  }

  public void send(OutgoingMessage message) {
    this.send(String.format("%s %s", message.command().code(), String.join(" ", message.parameters())));

    if (message.isWaiting()) {
      this.awaitingReplies.add(message);
    }
  }

  private void send(String message) {
    System.out.println(" -- " + message);
    this.socket.write(message);
    this.socket.write("\r\n");
  }
}
