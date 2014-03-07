package com.darylteo.vertx.ircbot.irc;

import com.darylteo.vertx.ircbot.irc.messages.Message;
import com.darylteo.vertx.ircbot.irc.messages.Prefix;
import org.vertx.java.core.buffer.Buffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by dteo on 6/03/2014.
 */
public class MessageParser {
  private final Map<String, CommandType> mappings = new HashMap<>();
  private final BufferTokenizer tokenizer = new BufferTokenizer();

  private static Pattern MESSAGE_PATTERN = Pattern.compile("^(:(?<prefix>[^\\s]+) )?(?<command>[^\\s]+) (?<parameters>.+)$");

  public MessageParser() {
    // basic types
    mappings.put("ADMIN", CommandType.ADMIN);
    mappings.put("AWAY", CommandType.AWAY);
    mappings.put("CONNECT", CommandType.CONNECT);
    mappings.put("DIE", CommandType.DIE);
    mappings.put("ERROR", CommandType.ERROR);
    mappings.put("INFO", CommandType.INFO);
    mappings.put("INVITE", CommandType.INVITE);
    mappings.put("ISON", CommandType.ISON);
    mappings.put("JOIN", CommandType.JOIN);
    mappings.put("KICK", CommandType.KICK);
    mappings.put("KILL", CommandType.KILL);
    mappings.put("LINKS", CommandType.LINKS);
    mappings.put("LIST", CommandType.LIST);
    mappings.put("LUSERS", CommandType.LUSERS);
    mappings.put("MODE", CommandType.MODE);
    mappings.put("MOTD", CommandType.MOTD);
    mappings.put("NAMES", CommandType.NAMES);
    mappings.put("NICK", CommandType.NICK);
    mappings.put("NOTICE", CommandType.NOTICE);
    mappings.put("OPER", CommandType.OPER);
    mappings.put("PART", CommandType.PART);
    mappings.put("PASS", CommandType.PASS);
    mappings.put("PING", CommandType.PING);
    mappings.put("PONG", CommandType.PONG);
    mappings.put("PRIVMSG", CommandType.PRIVMSG);
    mappings.put("QUIT", CommandType.QUIT);
    mappings.put("REHASH", CommandType.REHASH);
    mappings.put("RESTART", CommandType.RESTART);
    mappings.put("SERVICE", CommandType.SERVICE);
    mappings.put("SERVLIST", CommandType.SERVLIST);
    mappings.put("SQUERY", CommandType.SQUERY);
    mappings.put("SQUIT", CommandType.SQUIT);
    mappings.put("STATS", CommandType.STATS);
    mappings.put("SUMMON", CommandType.SUMMON);
    mappings.put("TIME", CommandType.TIME);
    mappings.put("TOPIC", CommandType.TOPIC);
    mappings.put("TRACE", CommandType.TRACE);
    mappings.put("USER", CommandType.USER);
    mappings.put("USERHOST", CommandType.USERHOST);
    mappings.put("USERS", CommandType.USERS);
    mappings.put("VERSION", CommandType.VERSION);
    mappings.put("WALLOPS", CommandType.WALLOPS);
    mappings.put("WHO", CommandType.WHO);
    mappings.put("WHOIS", CommandType.WHOIS);
    mappings.put("WHOWAS", CommandType.WHOWAS);

    // numeric replies
    mappings.put("001", CommandType.RPL_WELCOME);
    mappings.put("002", CommandType.RPL_YOURHOST);
    mappings.put("003", CommandType.RPL_CREATED);
    mappings.put("004", CommandType.RPL_MYINFO);
    mappings.put("005", CommandType.RPL_BOUNCE);
    mappings.put("302", CommandType.RPL_USERHOST);
    mappings.put("303", CommandType.RPL_ISON);
    mappings.put("301", CommandType.RPL_AWAY);
    mappings.put("305", CommandType.RPL_UNAWAY);
    mappings.put("306", CommandType.RPL_NOWAWAY);
    mappings.put("311", CommandType.RPL_WHOISUSER);
    mappings.put("312", CommandType.RPL_WHOISSERVER);
    mappings.put("313", CommandType.RPL_WHOISOPERATOR);
    mappings.put("317", CommandType.RPL_WHOISIDLE);
    mappings.put("318", CommandType.RPL_ENDOFWHOIS);
    mappings.put("319", CommandType.RPL_WHOISCHANNELS);
    mappings.put("314", CommandType.RPL_WHOWASUSER);
    mappings.put("369", CommandType.RPL_ENDOFWHOWAS);
    mappings.put("321", CommandType.RPL_LISTSTART);
    mappings.put("322", CommandType.RPL_LIST);
    mappings.put("323", CommandType.RPL_LISTEND);
    mappings.put("325", CommandType.RPL_UNIQOPIS);
    mappings.put("324", CommandType.RPL_CHANNELMODEIS);
    mappings.put("331", CommandType.RPL_NOTOPIC);
    mappings.put("332", CommandType.RPL_TOPIC);
    mappings.put("341", CommandType.RPL_INVITING);
    mappings.put("342", CommandType.RPL_SUMMONING);
    mappings.put("346", CommandType.RPL_INVITELIST);
    mappings.put("347", CommandType.RPL_ENDOFINVITELIST);
    mappings.put("348", CommandType.RPL_EXCEPTLIST);
    mappings.put("349", CommandType.RPL_ENDOFEXCEPTLIST);
    mappings.put("351", CommandType.RPL_VERSION);
    mappings.put("352", CommandType.RPL_WHOREPLY);
    mappings.put("315", CommandType.RPL_ENDOFWHO);
    mappings.put("353", CommandType.RPL_NAMREPLY);
    mappings.put("366", CommandType.RPL_ENDOFNAMES);
    mappings.put("364", CommandType.RPL_LINKS);
    mappings.put("365", CommandType.RPL_ENDOFLINKS);
    mappings.put("367", CommandType.RPL_BANLIST);
    mappings.put("368", CommandType.RPL_ENDOFBANLIST);
    mappings.put("371", CommandType.RPL_INFO);
    mappings.put("374", CommandType.RPL_ENDOFINFO);
    mappings.put("375", CommandType.RPL_MOTDSTART);
    mappings.put("372", CommandType.RPL_MOTD);
    mappings.put("376", CommandType.RPL_ENDOFMOTD);
    mappings.put("381", CommandType.RPL_YOUREOPER);
    mappings.put("382", CommandType.RPL_REHASHING);
    mappings.put("383", CommandType.RPL_YOURESERVICE);
    mappings.put("391", CommandType.RPL_TIME);
    mappings.put("392", CommandType.RPL_USERSSTART);
    mappings.put("393", CommandType.RPL_USERS);
    mappings.put("394", CommandType.RPL_ENDOFUSERS);
    mappings.put("395", CommandType.RPL_NOUSERS);
    mappings.put("200", CommandType.RPL_TRACELINK);
    mappings.put("201", CommandType.RPL_TRACECONNECTING);
    mappings.put("202", CommandType.RPL_TRACEHANDSHAKE);
    mappings.put("203", CommandType.RPL_TRACEUNKNOWN);
    mappings.put("204", CommandType.RPL_TRACEOPERATOR);
    mappings.put("205", CommandType.RPL_TRACEUSER);
    mappings.put("206", CommandType.RPL_TRACESERVER);
    mappings.put("207", CommandType.RPL_TRACESERVICE);
    mappings.put("208", CommandType.RPL_TRACENEWTYPE);
    mappings.put("209", CommandType.RPL_TRACECLASS);
    mappings.put("210", CommandType.RPL_TRACERECONNECT);
    mappings.put("261", CommandType.RPL_TRACELOG);
    mappings.put("262", CommandType.RPL_TRACEEND);
    mappings.put("211", CommandType.RPL_STATSLINKINFO);
    mappings.put("212", CommandType.RPL_STATSCOMMANDS);
    mappings.put("219", CommandType.RPL_ENDOFSTATS);
    mappings.put("242", CommandType.RPL_STATSUPTIME);
    mappings.put("243", CommandType.RPL_STATSOLINE);
    mappings.put("221", CommandType.RPL_UMODEIS);
    mappings.put("234", CommandType.RPL_SERVLIST);
    mappings.put("235", CommandType.RPL_SERVLISTEND);
    mappings.put("251", CommandType.RPL_LUSERCLIENT);
    mappings.put("252", CommandType.RPL_LUSEROP);
    mappings.put("253", CommandType.RPL_LUSERUNKNOWN);
    mappings.put("254", CommandType.RPL_LUSERCHANNELS);
    mappings.put("255", CommandType.RPL_LUSERME);
    mappings.put("256", CommandType.RPL_ADMINME);
    mappings.put("257", CommandType.RPL_ADMINLOC1);
    mappings.put("258", CommandType.RPL_ADMINLOC2);
    mappings.put("259", CommandType.RPL_ADMINEMAIL);
    mappings.put("263", CommandType.RPL_TRYAGAIN);
    mappings.put("401", CommandType.ERR_NOSUCHNICK);
    mappings.put("402", CommandType.ERR_NOSUCHSERVER);
    mappings.put("403", CommandType.ERR_NOSUCHCHANNEL);
    mappings.put("404", CommandType.ERR_CANNOTSENDTOCHAN);
    mappings.put("405", CommandType.ERR_TOOMANYCHANNELS);
    mappings.put("406", CommandType.ERR_WASNOSUCHNICK);
    mappings.put("407", CommandType.ERR_TOOMANYTARGETS);
    mappings.put("408", CommandType.ERR_NOSUCHSERVICE);
    mappings.put("409", CommandType.ERR_NOORIGIN);
    mappings.put("411", CommandType.ERR_NORECIPIENT);
    mappings.put("412", CommandType.ERR_NOTEXTTOSEND);
    mappings.put("413", CommandType.ERR_NOTOPLEVEL);
    mappings.put("414", CommandType.ERR_WILDTOPLEVEL);
    mappings.put("415", CommandType.ERR_BADMASK);
    mappings.put("421", CommandType.ERR_UNKNOWNCOMMAND);
    mappings.put("422", CommandType.ERR_NOMOTD);
    mappings.put("423", CommandType.ERR_NOADMININFO);
    mappings.put("424", CommandType.ERR_FILEERROR);
    mappings.put("431", CommandType.ERR_NONICKNAMEGIVEN);
    mappings.put("432", CommandType.ERR_ERRONEUSNICKNAME);
    mappings.put("433", CommandType.ERR_NICKNAMEINUSE);
    mappings.put("436", CommandType.ERR_NICKCOLLISION);
    mappings.put("437", CommandType.ERR_UNAVAILRESOURCE);
    mappings.put("441", CommandType.ERR_USERNOTINCHANNEL);
    mappings.put("442", CommandType.ERR_NOTONCHANNEL);
    mappings.put("443", CommandType.ERR_USERONCHANNEL);
    mappings.put("444", CommandType.ERR_NOLOGIN);
    mappings.put("445", CommandType.ERR_SUMMONDISABLED);
    mappings.put("446", CommandType.ERR_USERSDISABLED);
    mappings.put("451", CommandType.ERR_NOTREGISTERED);
    mappings.put("461", CommandType.ERR_NEEDMOREPARAMS);
    mappings.put("462", CommandType.ERR_ALREADYREGISTRED);
    mappings.put("463", CommandType.ERR_NOPERMFORHOST);
    mappings.put("464", CommandType.ERR_PASSWDMISMATCH);
    mappings.put("465", CommandType.ERR_YOUREBANNEDCREEP);
    mappings.put("466", CommandType.ERR_YOUWILLBEBANNED);
    mappings.put("467", CommandType.ERR_KEYSET);
    mappings.put("471", CommandType.ERR_CHANNELISFULL);
    mappings.put("472", CommandType.ERR_UNKNOWNMODE);
    mappings.put("473", CommandType.ERR_INVITEONLYCHAN);
    mappings.put("474", CommandType.ERR_BANNEDFROMCHAN);
    mappings.put("475", CommandType.ERR_BADCHANNELKEY);
    mappings.put("476", CommandType.ERR_BADCHANMASK);
    mappings.put("477", CommandType.ERR_NOCHANMODES);
    mappings.put("478", CommandType.ERR_BANLISTFULL);
    mappings.put("481", CommandType.ERR_NOPRIVILEGES);
    mappings.put("482", CommandType.ERR_CHANOPRIVSNEEDED);
    mappings.put("483", CommandType.ERR_CANTKILLSERVER);
    mappings.put("484", CommandType.ERR_RESTRICTED);
    mappings.put("485", CommandType.ERR_UNIQOPPRIVSNEEDED);
    mappings.put("491", CommandType.ERR_NOOPERHOST);
    mappings.put("501", CommandType.ERR_UMODEUNKNOWNFLAG);
    mappings.put("502", CommandType.ERR_USERSDONTMATCH);
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
    Prefix prefix = null;
    CommandType command = null;
    String[] parameters = null;

    for (int i = 0; i < tokens.size(); i++) {
      String current = tokens.get(i);

      if (i == 0 && current.startsWith(":")) {
        prefix = new Prefix(current);
      } else if (command == null) {
        command = mappings.containsKey(current) ? mappings.get(current) : CommandType._UNKNOWN;
      } else {
        parameters = tokens.subList(i, tokens.size()).toArray(new String[0]);
        break;
      }
    }

    return new Message(prefix, command, parameters);
  }

  private List<String> tokenize(String line) {
    List<String> tokens = new LinkedList<>();

    while (line.length() > 0) {
      // EXIT: detect trailing parameter, break if found.
      if (line.startsWith(":") && !tokens.isEmpty()) {
        // eat everything else, final token
        tokens.add(line);
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
