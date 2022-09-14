package ssta.xwdgame.main;

import ssta.xwdgame.game.Game;
import ssta.xwdgame.ui.XwdFrame;

/**
 * This class exists so you can run the game, with or without a debugger without connecting a bot to twitch.
 * This can be useful in order to test changes.
 */
public class TestMain {
  public static void main(String[] args) {
    Game game = new Game();
    new XwdFrame(game);
  }
}
