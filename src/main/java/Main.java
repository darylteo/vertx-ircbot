import org.vertx.java.core.Future;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.core.net.NetSocket;
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

        NetSocket socket = result.result();

        socket.dataHandler(buffer -> {
          System.out.println(buffer);z
        });

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
