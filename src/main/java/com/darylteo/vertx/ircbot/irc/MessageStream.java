package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import org.vertx.java.core.Handler;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by dteo on 9/03/2014.
 */
public class MessageStream {
  private Predicate<Message> takePredicate;
  private Predicate<Message> untilPredicate;

  private List<Message> replies = new LinkedList<>();

  private Handler<List<Message>> handler;

  //
  // Accessors
  public boolean isWaiting() {
    return this.handler != null;
  }

  //
  // Constructors
  public MessageStream() {

  }

  public MessageStream take(Predicate<Message> predicate) {
    this.takePredicate = predicate;
    return this;
  }

  public MessageStream until(Predicate<Message> predicate) {
    this.untilPredicate = predicate;
    return this;
  }

  public void then(Handler<List<Message>> handler) {
    this.handler = handler;
  }

  public void handle(Message message) {
    if (this.untilPredicate.test(message)) {
      // test if this message marks the end of this stream
      this.handler.handle(this.replies);
      this.handler = null;
    } else if (this.takePredicate.test(message)) {
      // test if this message should be taken
      this.replies.add(message);
    }
  }
}
