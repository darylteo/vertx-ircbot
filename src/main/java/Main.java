import com.darylteo.vertx.ircbot.irc.IRCClient;
import org.vertx.java.platform.Verticle;

/**
 * Created by dteo on 6/03/2014.
 */
public class Main extends Verticle {
  private IRCClient client;

  private String nick = "vertxbot";
  private String name = "Daryl Teo";

  @Override
  public void start() {
    IRCClient client = new IRCClient(this.vertx, nick, name, result -> this.joinChannels(result));
  }

  @Override
  public void stop() {
    if (this.client != null) {
      this.client.quit("Shutting Down");
    }

    super.stop();
  }

  private void joinChannels(IRCClient client) {
    client.join("#vertxbot");
  }

}
