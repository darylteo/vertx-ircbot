package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetClient;
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
  private Map<String, Channel> channels = new HashMap<>();

  //
  // Constructors
  public IRCClient(Vertx vertx, Handler<IRCClient> handler) {
    NetClient client = vertx.createNetClient();

    client.connect(8000, "irc.freenode.org", result -> {
      if (result.succeeded()) {
        System.out.println("Succeeded in connecting to server");

        this.init(result.result());
        this.ident("vertxbot", "Daryl Teo");

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
    this.bind("PING", message ->
      this.send("PONG", message.parameters())
    );

    // pass other messages to channels/subscribers
    socket.dataHandler(buffer -> this.handle(buffer));
  }

  //
  // CommandType
  public void ident(String nick, String real) {
    this.send("NICK", nick);
    this.send("USER", nick, "0", "*", ":" + real);
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
    this.handlers.put(command.toUpperCase(), handler);
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
  }

  //
  // Sending Methods
  public void send(String command, String... parameters) {
    send(String.format("%s %s", command, String.join(" ", parameters)));
  }

  private void send(String message) {
    System.out.println(" -- " + message);
    this.socket.write(message);
    this.socket.write("\r\n");
  }
}
