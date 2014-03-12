package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.types.PrivMsg;
import org.vertx.java.core.buffer.Buffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dteo on 6/03/2014.
 */
public class MessageParser {
  private final Map<String, CommandType> codes = new HashMap<>();
  private final Map<CommandType, CommandToMessageFunction<?>> types = new HashMap<>();

  private final BufferTokenizer tokenizer = new BufferTokenizer();

  @FunctionalInterface
  private interface CommandToMessageFunction<T extends Message> {
    T apply(String prefix, String[] args);
  }

  public MessageParser() {
    // basic types
    codes.put("ADMIN", CommandType.ADMIN);
    codes.put("AWAY", CommandType.AWAY);
    codes.put("CONNECT", CommandType.CONNECT);
    codes.put("DIE", CommandType.DIE);
    codes.put("ERROR", CommandType.ERROR);
    codes.put("INFO", CommandType.INFO);
    codes.put("INVITE", CommandType.INVITE);
    codes.put("ISON", CommandType.ISON);
    codes.put("JOIN", CommandType.JOIN);
    codes.put("KICK", CommandType.KICK);
    codes.put("KILL", CommandType.KILL);
    codes.put("LINKS", CommandType.LINKS);
    codes.put("LIST", CommandType.LIST);
    codes.put("MODE", CommandType.MODE);
    codes.put("MOTD", CommandType.MOTD);
    codes.put("NAMES", CommandType.NAMES);
    codes.put("NICK", CommandType.NICK);
    codes.put("NOTICE", CommandType.NOTICE);
    codes.put("OPER", CommandType.OPER);
    codes.put("PART", CommandType.PART);
    codes.put("PASS", CommandType.PASS);
    codes.put("PING", CommandType.PING);
    codes.put("PONG", CommandType.PONG);
    codes.put("PRIVMSG", CommandType.PRIVMSG);
    codes.put("QUIT", CommandType.QUIT);
    codes.put("REHASH", CommandType.REHASH);
    codes.put("RESTART", CommandType.RESTART);
    codes.put("SERVICE", CommandType.SERVICE);
    codes.put("SERVLIST", CommandType.SERVLIST);
    codes.put("SQUERY", CommandType.SQUERY);
    codes.put("SQUIT", CommandType.SQUIT);
    codes.put("STATS", CommandType.STATS);
    codes.put("SUMMON", CommandType.SUMMON);
    codes.put("TIME", CommandType.TIME);
    codes.put("TOPIC", CommandType.TOPIC);
    codes.put("TRACE", CommandType.TRACE);
    codes.put("USER", CommandType.USER);
    codes.put("USERHOST", CommandType.USERHOST);
    codes.put("USERS", CommandType.USERS);
    codes.put("VERSION", CommandType.VERSION);
    codes.put("WALLOPS", CommandType.WALLOPS);
    codes.put("WHO", CommandType.WHO);
    codes.put("WHOIS", CommandType.WHOIS);
    codes.put("WHOWAS", CommandType.WHOWAS);

    // numeric replies
    codes.put("001", CommandType.RPL_WELCOME);
    codes.put("002", CommandType.RPL_YOURHOST);
    codes.put("003", CommandType.RPL_CREATED);
    codes.put("004", CommandType.RPL_MYINFO);
    codes.put("005", CommandType.RPL_BOUNCE);
    codes.put("302", CommandType.RPL_USERHOST);
    codes.put("303", CommandType.RPL_ISON);
    codes.put("301", CommandType.RPL_AWAY);
    codes.put("305", CommandType.RPL_UNAWAY);
    codes.put("306", CommandType.RPL_NOWAWAY);
    codes.put("311", CommandType.RPL_WHOISUSER);
    codes.put("312", CommandType.RPL_WHOISSERVER);
    codes.put("313", CommandType.RPL_WHOISOPERATOR);
    codes.put("317", CommandType.RPL_WHOISIDLE);
    codes.put("318", CommandType.RPL_ENDOFWHOIS);
    codes.put("319", CommandType.RPL_WHOISCHANNELS);
    codes.put("314", CommandType.RPL_WHOWASUSER);
    codes.put("369", CommandType.RPL_ENDOFWHOWAS);
    codes.put("321", CommandType.RPL_LISTSTART);
    codes.put("322", CommandType.RPL_LIST);
    codes.put("323", CommandType.RPL_LISTEND);
    codes.put("325", CommandType.RPL_UNIQOPIS);
    codes.put("324", CommandType.RPL_CHANNELMODEIS);
    codes.put("331", CommandType.RPL_NOTOPIC);
    codes.put("332", CommandType.RPL_TOPIC);
    codes.put("341", CommandType.RPL_INVITING);
    codes.put("342", CommandType.RPL_SUMMONING);
    codes.put("346", CommandType.RPL_INVITELIST);
    codes.put("347", CommandType.RPL_ENDOFINVITELIST);
    codes.put("348", CommandType.RPL_EXCEPTLIST);
    codes.put("349", CommandType.RPL_ENDOFEXCEPTLIST);
    codes.put("351", CommandType.RPL_VERSION);
    codes.put("352", CommandType.RPL_WHOREPLY);
    codes.put("315", CommandType.RPL_ENDOFWHO);
    codes.put("353", CommandType.RPL_NAMREPLY);
    codes.put("366", CommandType.RPL_ENDOFNAMES);
    codes.put("364", CommandType.RPL_LINKS);
    codes.put("365", CommandType.RPL_ENDOFLINKS);
    codes.put("367", CommandType.RPL_BANLIST);
    codes.put("368", CommandType.RPL_ENDOFBANLIST);
    codes.put("371", CommandType.RPL_INFO);
    codes.put("374", CommandType.RPL_ENDOFINFO);
    codes.put("375", CommandType.RPL_MOTDSTART);
    codes.put("372", CommandType.RPL_MOTD);
    codes.put("376", CommandType.RPL_ENDOFMOTD);
    codes.put("381", CommandType.RPL_YOUREOPER);
    codes.put("382", CommandType.RPL_REHASHING);
    codes.put("383", CommandType.RPL_YOURESERVICE);
    codes.put("391", CommandType.RPL_TIME);
    codes.put("392", CommandType.RPL_USERSSTART);
    codes.put("393", CommandType.RPL_USERS);
    codes.put("394", CommandType.RPL_ENDOFUSERS);
    codes.put("395", CommandType.RPL_NOUSERS);
    codes.put("200", CommandType.RPL_TRACELINK);
    codes.put("201", CommandType.RPL_TRACECONNECTING);
    codes.put("202", CommandType.RPL_TRACEHANDSHAKE);
    codes.put("203", CommandType.RPL_TRACEUNKNOWN);
    codes.put("204", CommandType.RPL_TRACEOPERATOR);
    codes.put("205", CommandType.RPL_TRACEUSER);
    codes.put("206", CommandType.RPL_TRACESERVER);
    codes.put("207", CommandType.RPL_TRACESERVICE);
    codes.put("208", CommandType.RPL_TRACENEWTYPE);
    codes.put("209", CommandType.RPL_TRACECLASS);
    codes.put("210", CommandType.RPL_TRACERECONNECT);
    codes.put("261", CommandType.RPL_TRACELOG);
    codes.put("262", CommandType.RPL_TRACEEND);
    codes.put("211", CommandType.RPL_STATSLINKINFO);
    codes.put("212", CommandType.RPL_STATSCOMMANDS);
    codes.put("219", CommandType.RPL_ENDOFSTATS);
    codes.put("242", CommandType.RPL_STATSUPTIME);
    codes.put("243", CommandType.RPL_STATSOLINE);
    codes.put("221", CommandType.RPL_UMODEIS);
    codes.put("234", CommandType.RPL_SERVLIST);
    codes.put("235", CommandType.RPL_SERVLISTEND);
    codes.put("251", CommandType.RPL_LUSERCLIENT);
    codes.put("252", CommandType.RPL_LUSEROP);
    codes.put("253", CommandType.RPL_LUSERUNKNOWN);
    codes.put("254", CommandType.RPL_LUSERCHANNELS);
    codes.put("255", CommandType.RPL_LUSERME);
    codes.put("256", CommandType.RPL_ADMINME);
    codes.put("257", CommandType.RPL_ADMINLOC1);
    codes.put("258", CommandType.RPL_ADMINLOC2);
    codes.put("259", CommandType.RPL_ADMINEMAIL);
    codes.put("263", CommandType.RPL_TRYAGAIN);
    codes.put("401", CommandType.ERR_NOSUCHNICK);
    codes.put("402", CommandType.ERR_NOSUCHSERVER);
    codes.put("403", CommandType.ERR_NOSUCHCHANNEL);
    codes.put("404", CommandType.ERR_CANNOTSENDTOCHAN);
    codes.put("405", CommandType.ERR_TOOMANYCHANNELS);
    codes.put("406", CommandType.ERR_WASNOSUCHNICK);
    codes.put("407", CommandType.ERR_TOOMANYTARGETS);
    codes.put("408", CommandType.ERR_NOSUCHSERVICE);
    codes.put("409", CommandType.ERR_NOORIGIN);
    codes.put("411", CommandType.ERR_NORECIPIENT);
    codes.put("412", CommandType.ERR_NOTEXTTOSEND);
    codes.put("413", CommandType.ERR_NOTOPLEVEL);
    codes.put("414", CommandType.ERR_WILDTOPLEVEL);
    codes.put("415", CommandType.ERR_BADMASK);
    codes.put("421", CommandType.ERR_UNKNOWNCOMMAND);
    codes.put("422", CommandType.ERR_NOMOTD);
    codes.put("423", CommandType.ERR_NOADMININFO);
    codes.put("424", CommandType.ERR_FILEERROR);
    codes.put("431", CommandType.ERR_NONICKNAMEGIVEN);
    codes.put("432", CommandType.ERR_ERRONEUSNICKNAME);
    codes.put("433", CommandType.ERR_NICKNAMEINUSE);
    codes.put("436", CommandType.ERR_NICKCOLLISION);
    codes.put("437", CommandType.ERR_UNAVAILRESOURCE);
    codes.put("441", CommandType.ERR_USERNOTINCHANNEL);
    codes.put("442", CommandType.ERR_NOTONCHANNEL);
    codes.put("443", CommandType.ERR_USERONCHANNEL);
    codes.put("444", CommandType.ERR_NOLOGIN);
    codes.put("445", CommandType.ERR_SUMMONDISABLED);
    codes.put("446", CommandType.ERR_USERSDISABLED);
    codes.put("451", CommandType.ERR_NOTREGISTERED);
    codes.put("461", CommandType.ERR_NEEDMOREPARAMS);
    codes.put("462", CommandType.ERR_ALREADYREGISTRED);
    codes.put("463", CommandType.ERR_NOPERMFORHOST);
    codes.put("464", CommandType.ERR_PASSWDMISMATCH);
    codes.put("465", CommandType.ERR_YOUREBANNEDCREEP);
    codes.put("466", CommandType.ERR_YOUWILLBEBANNED);
    codes.put("467", CommandType.ERR_KEYSET);
    codes.put("471", CommandType.ERR_CHANNELISFULL);
    codes.put("472", CommandType.ERR_UNKNOWNMODE);
    codes.put("473", CommandType.ERR_INVITEONLYCHAN);
    codes.put("474", CommandType.ERR_BANNEDFROMCHAN);
    codes.put("475", CommandType.ERR_BADCHANNELKEY);
    codes.put("476", CommandType.ERR_BADCHANMASK);
    codes.put("477", CommandType.ERR_NOCHANMODES);
    codes.put("478", CommandType.ERR_BANLISTFULL);
    codes.put("481", CommandType.ERR_NOPRIVILEGES);
    codes.put("482", CommandType.ERR_CHANOPRIVSNEEDED);
    codes.put("483", CommandType.ERR_CANTKILLSERVER);
    codes.put("484", CommandType.ERR_RESTRICTED);
    codes.put("485", CommandType.ERR_UNIQOPPRIVSNEEDED);
    codes.put("491", CommandType.ERR_NOOPERHOST);
    codes.put("501", CommandType.ERR_UMODEUNKNOWNFLAG);
    codes.put("502", CommandType.ERR_USERSDONTMATCH);

    // mappings
    types.put(CommandType.PRIVMSG, (p, args) -> new PrivMsg(p, args));
  }

  public void append(Buffer buffer) {
    this.tokenizer.append(buffer);
  }

  public boolean hasNext() {
    return this.tokenizer.hasNext();
  }

  public Message next() {
    return this.parse(this.tokenize(tokenizer.next()));
  }

  //
  // Parsing the line
  private Message parse(List<String> tokens) {
    String prefix = null;
    CommandType command = null;
    String[] parameters = null;

    for (int i = 0; i < tokens.size(); i++) {
      String current = tokens.get(i);

      if (i == 0 && current.startsWith(":")) {
        prefix = current.substring(1);
      } else if (command == null) {
        command = codes.containsKey(current) ? codes.get(current) : CommandType._UNKNOWN;
      } else {
        parameters = tokens.subList(i, tokens.size()).toArray(new String[0]);
        break;
      }
    }

    // use a specified mapping if available
    if (types.containsKey(command)) {
      return types.get(command).apply(prefix, parameters);
    }

    // else return a generic message type
    return new Message(prefix, command, parameters);
  }

  private List<String> tokenize(String line) {
    List<String> tokens = new LinkedList<>();

    while (line.length() > 0) {
      // EXIT: detect trailing parameter, break if found.
      if (line.startsWith(":") && !tokens.isEmpty()) {
        // eat everything else, final token
        // ignore the :
        tokens.add(line.substring(1));
        break;
      }

      // find next parameter
      int space = line.indexOf(" ");

      // EXIT: detect final parameter (without spaces)
      if (space == -1) {
        tokens.add(line);
        break;
      }

      tokens.add(line.substring(0, space));
      line = line.substring(space + 1);
    }

    return tokens;
  }
}
