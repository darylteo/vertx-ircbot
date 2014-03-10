import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.promises.java.Promise;
import com.darylteo.vertx.promises.java.functions.PromiseAction;
import org.vertx.java.platform.Verticle;
import rx.Observable;

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
    IRCClient _client = new IRCClient(this.vertx, nick, name, client -> {
      // bind privmsg
      client
        .stream()
        .subscribe(message -> {
          // perform this on each message
          System.out.println("Log: " + message);
        });

      // bind privmsg
      client
        .stream()
        .filter(message -> message.isCommand(CommandType.PRIVMSG))
        .subscribe(message -> {
          // perform this on each message
          System.out.println(message);
        });

      // join channels
      this.waitForMOTD(client).subscribe(messages -> {
        System.out.println("MOTD Finished");
        this.joinChannel(client, "#vertxbot");
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

  private Observable<List<Message>> waitForMOTD(IRCClient client) {
    return client.stream()
      .takeWhile(message -> !message.isCommand(CommandType.RPL_ENDOFMOTD))
      .toList();
  }

  private Promise<Void> joinChannels(IRCClient client) {
    return this
      .joinChannel(client, "#vertxbot")
      .then((PromiseAction<List<Message>>) messages -> {
        for (Message m : messages) {
          System.out.println("User in room" + m);
        }
      });
  }

  private Promise<List<Message>> joinChannel(IRCClient client, String channel) {
    Promise<List<Message>> promise = new Promise<>();

    client.join(channel);
    client.stream()
      .takeWhile(message -> !message.isCommand(CommandType.RPL_ENDOFNAMES))
      .filter(message -> message.isCommand(CommandType.RPL_NAMREPLY))
      .toList()
      .subscribe(promise);

    return promise;
  }

}
