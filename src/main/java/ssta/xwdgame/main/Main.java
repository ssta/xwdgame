package ssta.xwdgame.main;

import ssta.xwdgame.bot.XwdBot;
import ssta.xwdgame.game.Game;
import ssta.xwdgame.ui.XwdFrame;

public class Main {
  private static XwdBot xwdBot;

  public static void main(String[] args) {
    Game game = new Game();
    new XwdFrame(game);
    new Thread(() -> {
      xwdBot = new XwdBot(game);
      xwdBot.connect();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      xwdBot.joinChannel("#stephensta192");
      xwdBot.start();
    }).start();
  }
}
