import org.vertx.java.core.net.NetClient;
import org.vertx.java.platform.Verticle;

/**
 * Created by dteo on 6/03/2014.
 */
public class Main extends Verticle {
  @Override
  public void start() {
    NetClient client = vertx.createNetClient();
    
    client.connect(8000, "irc.freenode.org", result -> {
      if(result.succeeded()) {
        System.out.println("Succeeded in connecting to server");

        IRCClient server = new IRCClient(result.result());
        server.ident("vertxbot", "Daryl Teo");
      } else {
        System.out.println("Connection to irc server failed");
      }
    });
  }

  @Override
  public void stop() {
    super.stop();
  }

}
