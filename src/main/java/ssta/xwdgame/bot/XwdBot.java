package ssta.xwdgame.bot;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import ssta.xwdgame.game.Game;

import java.util.logging.Level;
import java.util.logging.Logger;

import static ssta.xwdgame.bot.Private.OAUTH_KEY;

public class XwdBot extends TwitchBot {

  private final Game game;

  public XwdBot(Game game) {
    this.game = game;
    this.setUsername("xwdBot");
    this.setOauth_Key(OAUTH_KEY);
  }

  @Override
  protected void onMessage(User user, Channel channel, String message) {
    if (!message.startsWith("~")) {
      onCommand(user, channel, message);
    } else if ("~score".equalsIgnoreCase(message.trim())) {
      displayUserScore(user, channel);
    } else if ("~wakekitty".equalsIgnoreCase(message.trim())) {
      dontWakeTheKitty(user, channel);
    } else if ("~ddh".equalsIgnoreCase(message.trim())) {
      sendMessage("It's a Ding Dang Hidden!", channel);
    } else if ("~petkitty".equalsIgnoreCase(message.trim())) {
      sendMessage("PUURRRRRRR", channel);
    } else if ("~bafta".equalsIgnoreCase(message)) {
      sendMessage("BAFTA::Something designed to confuse Americans and other aliens!", channel);
    } else if ("~fish".equalsIgnoreCase(message)) {
      sendMessage("Sorry, wrong channel.  Try https://www.twitch.tv/elderism instead", channel);
    }
  }

  @Override
  protected void onCommand(User user, Channel channel, String command) {
    Logger.getAnonymousLogger()
        .log(Level.INFO,
            String.format("received command '%s' from user '%s'", command, user));
    game.guess(command, game.getOrCreateUser(user.toString()));
  }

  private void displayUserScore(User user, Channel channel) {
    String score = game.getScores(Game.ScoreType.ALL_TIME)
        .stream()
        .filter(s -> username(user).equalsIgnoreCase(s.getUser()
            .getName()))
        .findAny()
        .map(s -> String.format("%d from %d clues solved with an average per clue of %.2f",
            s.getUserScore(), s.getClueCount(), ((float) s.getUserScore()) / s.getClueCount()))
        .orElse("");
    String message = String.format("@%s: Your current alltime score is: %s", user.toString(), score);
    sendMessage(message, channel);
  }

  private void dontWakeTheKitty(User user, Channel channel) {
    String message = String.format("@%s: Which part of 'DO NOT WAKE THE KITTY' was hard to understand?  DO NOT DO IT AGAIN!", username(user));
    sendMessage(message, channel);
  }

  private String username(User user) {
    return user.toString();
  }
}
