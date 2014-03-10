package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.ReplaySubject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by dteo on 9/03/2014.
 */
public class MessageStream extends Observable<Message> {
  private Predicate<Message> untilPredicate;

  private List<Message> replies = new LinkedList<>();

  private List<Subscriber<Message>> subscribers;
  private ReplaySubject<Message> subject;

  private boolean ended = false;

  public boolean isWaiting() {
    return !this.ended;
  }

  //
  // Constructors
  public static MessageStream create() {
    return new MessageStream(ReplaySubject.create());
  }

  public MessageStream(final ReplaySubject<Message> subject) {
    super(subscriber -> {
      subject.subscribe(subscriber);
    });

    this.subject = subject;
  }

  public MessageStream until(Predicate<Message> predicate) {
    this.untilPredicate = predicate;
    return this;
  }

  public void handle(Message message) {
    if (this.untilPredicate.test(message)) {
      this.subject.onCompleted();
      this.ended = true;
    } else {
      this.subject.onNext(message);
    }
  }
}
