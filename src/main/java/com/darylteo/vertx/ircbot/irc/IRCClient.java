package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.OutgoingMessage;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.core.net.NetSocket;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;
  private MessageParser parser = new MessageParser();

  private Map<CommandType, MessageHandler> handlers = new HashMap<>();
  private List<MessageStream> awaitingReplies = new LinkedList<>();

  private PublishSubject<Message> subject;

  //
  // Constructors
  public IRCClient(Vertx vertx, String nick, String name, Handler<IRCClient> handler) {
    NetClient client = vertx.createNetClient();
    this.subject = PublishSubject.create();

    this.stream()
      .filter(message -> message.isCommand(CommandType.PING))
      .subscribe(message -> this.send(CommandType.PONG, message.parameters()));

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

    // pass messages to channels/subscribers through the Subject
    socket.dataHandler(buffer -> this.handle(buffer));
  }

  public Observable<Message> stream() {
    return this.subject;
  }

  //
  // CommandType
  public void ident(String nick, String real) {
    this.send(CommandType.NICK, nick);
    this.send(CommandType.USER, nick, "0", "*", ":" + real);
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
    System.out.println("<-- :" + message);
    this.subject.onNext(message);
  }

  //
  // Sending Methods
  public IRCClient send(CommandType command, String... parameters) {
    this.send(String.format("%s %s", command.code(), String.join(" ", parameters)));
    return this;
  }

  public IRCClient send(String command, String... parameters) {
    this.send(String.format("%s %s", command, String.join(" ", parameters)));
    return this;
  }

  public IRCClient send(OutgoingMessage message) {
    this.send(String.format("%s %s", message.command(), String.join(" ", message.parameters())));
    return this;
  }

  private void send(String message) {
    System.out.println("--> : " + message);
    this.socket.write(message);
    this.socket.write("\r\n");
  }
}
