package com.darylteo.vertx.ircbot.bot.channels;

import com.darylteo.vertx.ircbot.irc.CommandType;
import com.darylteo.vertx.ircbot.irc.IRCClient;
import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.User;
import com.darylteo.vertx.ircbot.irc.messages.types.PrivMsg;
import rx.Observable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dteo on 7/03/2014.
 */
public class Channel {
  private IRCClient client;

  private String channel;
  private String nick;

  private List<User> users;
  private Observable<Message> stream;

  private Pattern commandPrefix = null;
  private Pattern whitespace = Pattern.compile("\\s+");

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

    try {
      this.commandPrefix = Pattern.compile(String.format("^\\s*\\!(?<command>.+)$", this.nick));
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.stream = client.stream()
      .filter(message -> accept(message));

    // privmsgs
    subscribePrivmsg(this.stream);

    // trigger join
    client.send(CommandType.JOIN, channel);

    refreshUsers();
  }

  public void registerPlugin(Plugin plugin) {
    this.stream.subscribe(plugin);
  }

  private void refreshUsers() {
    // subscribe
    this.client
      .send(CommandType.WHO, this.channel)
      .stream()
      .takeWhile(message -> !message.isCommand(CommandType.RPL_ENDOFWHO))
      .filter(message -> whoreply(message))
      .map(message -> new User(
        message.parameters(2), // username
        message.parameters(5), // nick
        message.parameters(3)) // host
      )
      .toSortedList()
      .subscribe(users -> {
        System.out.println("Users Get: " + users);
        this.users = users;
      });
  }

  private void subscribePrivmsg(Observable<Message> stream) {
    stream
      .filter(message -> message.isCommand(CommandType.PRIVMSG))
      .cast(PrivMsg.class)
      .filter(message -> {
        return message.getRecipient().equals(this.channel);
      })
      .subscribe(message -> handlePrivmsg(message));
  }

  private void handlePrivmsg(PrivMsg message) {
    System.out.println(
      "Message to " + message.getRecipient() +
        " from " + message.getSender() +
        " in channel " + this.channel +
        ": " + message.getMessage());

    Matcher matcher = this.commandPrefix.matcher(message.getMessage());
    if (matcher.matches()) {
      String[] commands = whitespace.split(matcher.group("command"));
      System.out.println("Command Received: " + commands);
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
