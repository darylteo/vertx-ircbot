import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import rx.Observable;

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

  private Observable<Message> stream;

  public Channel(IRCClient client, String channel) {
    this.client = client;
    this.channel = channel;

    this.stream = client.stream()
      .filter(message -> privmsg(message));

    // trigger join
    client.send(CommandType.JOIN, channel);

    refreshUsers();
  }

  private void refreshUsers() {
    // subscribe
    this.client
      .send(CommandType.WHO, this.channel)
      .stream()
      .takeWhile(message -> !message.isCommand(CommandType.RPL_ENDOFWHO))
      .filter(message -> whoreply(message))
      .map(message -> message.parameters(2))
      .toList()
      .subscribe(users -> {
        System.out.println("Users Get: " + users);
        this.users = users;
      });
  }

  // filters
  private boolean privmsg(Message message) {
    return message.isCommand(CommandType.PRIVMSG) && message.parameters(0).equals(this.channel);
  }

  private boolean whoreply(Message message) {
    return message.isCommand(CommandType.RPL_WHOREPLY) && message.parameters(1).equals(this.channel);
  }
}
