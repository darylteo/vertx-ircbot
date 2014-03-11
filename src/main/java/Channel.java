import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import rx.Subscription;

import java.util.List;

/**
 * Created by dteo on 7/03/2014.
 */
public class Channel {
  private String channel;

  public String getChannel() {
    return channel;
  }

  private List<String> users;

  public List<String> getUsers() {
    return users;
  }

  private IRCClient client;

  public Channel(IRCClient client, String channel) {
    this.client = client;
    this.channel = channel;

    // trigger join
    client.send(CommandType.JOIN, channel);

    refreshUsers();
  }

  private void refreshUsers() {
    // subscribe
    Subscription subscribe = this.client
      .send(CommandType.WHO, this.channel)
      .stream()
      .takeWhile(message -> !message.isCommand(CommandType.RPL_ENDOFWHO))
      .filter(message -> message.isCommand(CommandType.RPL_WHOREPLY))
      .map(message -> message.toString())
      .toList()
      .subscribe(users -> {
        System.out.println("Users Get: " + users);
        this.users = users;
      });
  }
}
