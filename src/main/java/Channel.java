import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.User;
import com.darylteo.vertx.ircbot.irc.messages.types.PrivMsg;
import rx.Observable;

import java.util.List;

/**
 * Created by dteo on 7/03/2014.
 */
public class Channel {
  private IRCClient client;

  private String channel;
  private String nick;

  private List<User> users;
  private Observable<Message> stream;

  public String getChannel() {
    return channel;
  }

  public List<User> getUsers() {
    return users;
  }

  public Channel(IRCClient client, String nick, String channel) {
    this.client = client;
    this.nick = nick;
    this.channel = channel;

    this.stream = client.stream()
      .filter(message -> accept(message));

    // privmsgs
    this.stream
      .filter(message -> message.isCommand(CommandType.PRIVMSG))
      .cast(PrivMsg.class)
      .filter(message -> message.getRecipient().equals(this.channel))
      .subscribe(message -> handlePrivmsg(message));

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
      .map(message -> new User(
        message.parameters(3), // username
        message.parameters(6), // nick
        message.parameters(4)) // host
      )
      .toSortedList()
      .subscribe(users -> {
        System.out.println("Users Get: " + users);
        this.users = users;
      });
  }

  private void handlePrivmsg(PrivMsg message) {
    System.out.println(
      "Message to " + message.getRecipient() +
        " from " + message.getSender() +
        " in channel " + this.channel +
        ": " + message.getMessage());

    String text = message.getMessage();
    if (text.trim().startsWith(this.nick)) {
      this.client.send(CommandType.PRIVMSG, this.channel, ":Hello " + message.getSender());
    }
  }

  // filters
  private boolean accept(Message message) {
    return message.isCommand(
      // messages
      CommandType.PRIVMSG,
      CommandType.JOIN,
      CommandType.PART,

      // replies
      CommandType.RPL_WHOREPLY
    );
  }

  private boolean whoreply(Message message) {
    return message.isCommand(CommandType.RPL_WHOREPLY) && message.parameters(1).equals(this.channel);
  }
}
