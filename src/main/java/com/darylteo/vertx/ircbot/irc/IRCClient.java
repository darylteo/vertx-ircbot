package com.darylteo.vertx.ircbot.irc;

import org.vertx.java.core.net.NetSocket;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;

  private CommandHandler handler;
  private CommandParser parser = new CommandParser();

  public IRCClient(NetSocket socket) {
    this.socket = socket;

    socket.dataHandler(buffer -> {
      this.parser.append(buffer);

      while (this.parser.hasNext()) {
        System.out.println(this.parser.next());
      }
    });
  }

  public void ident(String nick, String real) {
    this.send("NICK " + nick);
    this.send("USER " + nick + " 0 * :" + real);
  }

  public void quit(String reason) {
    send("QUIT " + reason);
  }

  public void commandHandler(CommandHandler handler) {
    this.handler = handler;
  }

  private void send(String message) {
    System.out.println(" -- " + message);
    this.socket.write(message);
    this.socket.write("\n");
  }
}
