import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.promises.java.Promise;
import com.darylteo.vertx.promises.java.functions.PromiseAction;
import org.vertx.java.platform.Verticle;

import java.util.List;

/**
 * Created by dteo on 6/03/2014.
 */
public class Main extends Verticle {
  private IRCClient client;

  private String nick = "vertxbot";
  private String name = "Daryl Teo";

  @Override
  public void start() {
    IRCClient client = new IRCClient(this.vertx, nick, name, result -> {

      this.getMOTD(result).then((PromiseAction<List<Message>>) messages -> {
        for (Message m : messages) {
          System.out.println(m);
        }

        this.joinChannels(result);
      });
    });
  }

  @Override
  public void stop() {
    if (this.client != null) {
      this.client.quit("Shutting Down");
    }

    super.stop();
  }

  private Promise<List<Message>> getMOTD(IRCClient client) {
    Promise<List<Message>> promise = new Promise<>();

    client.stream()
      .take(message -> true)
      .until(message -> message.isCommand(CommandType.MODE))
      .then(messages -> promise.fulfill(messages));

    return promise;
  }

  private void joinChannels(IRCClient client) {
    this.joinChannel(client, "#vertxbot").then((PromiseAction<List<Message>>) messages -> {
      for (Message m : messages) {
        System.out.println("User in room" + m);
      }
    });
  }

  private Promise<List<Message>> joinChannel(IRCClient client, String channel) {
    Promise<List<Message>> promise = new Promise<>();

    client.join(channel);
    client.stream()
      .take(message -> message.isCommand(CommandType.RPL_NAMREPLY))
      .until(message -> message.isCommand(CommandType.RPL_ENDOFNAMES))
      .then(messages -> promise.fulfill(messages));

    return promise;
  }

}
