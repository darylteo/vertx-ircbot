import org.vertx.java.core.net.NetSocket;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;

  private CommandHandler handler;

  public IRCClient(NetSocket socket) {
    this.socket = socket;

    socket.dataHandler(buffer -> {
      System.out.println(buffer);
    });
  }

  public void ident(String nick, String real) {
    this.send("NICK " + nick);
    this.send("USER " + nick + " 0 * :" + real);
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
