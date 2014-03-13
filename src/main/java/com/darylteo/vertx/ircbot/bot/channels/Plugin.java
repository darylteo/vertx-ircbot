package com.darylteo.vertx.ircbot.bot.channels;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import rx.Observer;

/**
 * Created by dteo on 13/03/2014.
 */
public class Plugin implements Observer<Message> {
  @Override
  public void onCompleted() {
    
  }

  @Override
  public void onError(Throwable e) {

  }

  @Override
  public void onNext(Message message) {

  }
}
