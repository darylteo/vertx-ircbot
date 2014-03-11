import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import org.vertx.java.platform.Verticle;
import rx.Observable;

import java.util.LinkedList;
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
      //      client
      //        .stream()
      //        .subscribe(message -> {
      //          // perform this on each message
      //          System.out.println("Log: " + message);
      //        });

      // bind privmsg
      client
        .stream()
        .filter(message -> message.isCommand(CommandType.PRIVMSG))
        .subscribe(message -> {
          // perform this on each message
          System.out.println(message);
        });

      // join channels
      Observable<List<Message>> motd = this.waitForMOTD(client).first();

      Observable<Channel> channels = motd.flatMap(messages -> {
        for (Message m : messages) {
          System.out.println(m);
        }

        System.out.println("MOTD Finished");
        return this.joinChannels(client);
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

  private Observable<Channel> joinChannels(IRCClient client) {
    List<Channel> channels = new LinkedList<>();

    channels.add(new Channel(client, "#vertxbot"));

    return Observable.from(channels);
  }

}
