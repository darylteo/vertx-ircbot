import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dteo on 6/03/2014.
 */
public class IRCClient {
  private NetSocket socket;

  private CommandHandler handler;
  private CommandBuffer commands = new CommandBuffer();

  public IRCClient(NetSocket socket) {
    this.socket = socket;

    socket.dataHandler(buffer -> {
      this.commands.append(buffer);

      while(this.commands.hasNext()) {
        System.out.println(this.commands.next());
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
