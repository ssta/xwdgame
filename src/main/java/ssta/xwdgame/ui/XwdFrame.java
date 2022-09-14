package ssta.xwdgame.ui;

import processing.core.PApplet;
import ssta.xwdgame.entity.Clue;
import ssta.xwdgame.entity.Solved;
import ssta.xwdgame.game.Game;
import ssta.xwdgame.game.Score;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class XwdFrame extends PApplet {

  public static final int ANSWER_TEXT_SIZE = 40;
  public static final int BOX_GAP = 50;
  public static final int BOX_SIZE = 45;
  public static final int CLUE_FILL = 200;
  public static final int CLUE_TEXT_SIZE = 30;
  public static final int CLUE_Y = 100;
  public static final int COPYRIGHT_FILL = 150;
  public static final int COPYRIGHT_TEXT_SIZE = 10;
  public static final int COPYRIGHT_X = 1200;
  public static final int COPYRIGHT_Y = 700;
  private static final float NEXT_HINT_TIME_TEXT_SIZE = 20f;
  private static final int NEXT_HINT_TIME_FILL = 200;
  private static final float NEXT_HINT_TIME_X = 20f;
  private static final float NEXT_HINT_TIME_Y = 50f;
  private static final float TEXT_MAX_WIDTH = 1000;
  private final Game game;

  public XwdFrame(Game game) {
    this.game = game;
    String[] processingArgs = {"XwdGame"};
    PApplet.runSketch(processingArgs, this);
  }

  @Override
  public void settings() {
    size(1280, 720);
  }

  @Override
  public void draw() {
    clear();
    drawBasicScene();
    drawCurrentClue();
    drawSolvedClues();
    drawScores();
    drawNextHintTime();
  }

  private void drawBasicScene() {
    fill(COPYRIGHT_FILL);
    textSize(COPYRIGHT_TEXT_SIZE);
    textAlign(RIGHT, BOTTOM);
    text("XwdGame (c) 2022 - all rights wossnamed", COPYRIGHT_X, COPYRIGHT_Y);
  }

  private void drawCurrentClue() {
    game.update();
    Clue clue = game.getCurrentClue();
    pushMatrix();
    translate(50, 75);
    drawClue(clue, game.getCurrentAnswer(), game.clueFinished());
    popMatrix();
  }

  private void drawClue(Clue clue, String answerSoFar, boolean clueFinished) {
    drawTiles(answerSoFar, clueFinished);
    fill(CLUE_FILL);
    textAlign(LEFT, BOTTOM);
    int textSize = CLUE_TEXT_SIZE;
    textSize(CLUE_TEXT_SIZE);
    String clueText = clue.getClueText();
    clueText = clueText.replace("\\n", " ");
    while (textWidth(clueText) > TEXT_MAX_WIDTH) {
      textSize = textSize - 1;
      textSize(textSize);
    }
    text(clueText, 0, CLUE_Y);

    // author/puzzle citation
    fill(200);
    String citation;
    if (!clueFinished) {
      citation = String.format("%s -- %s; Last solved by %s %s",
          clue.getSetter(), clue.getXwdName(),
          clue.getSolvedByUser() == null ? "Nobody" : clue.getSolvedByUser().getName(),
          clue.getLastSolved() > 0 ? new SimpleDateFormat(" dd MMM yyyy").format(new Date(clue.getLastSolved())) : "");
    } else {
      citation = String.format("%s -- %s", clue.getSetter(), clue.getXwdName());
    }
    textAlign(LEFT, BOTTOM);
    textSize(15);
    text(citation, 0, 130);
  }

  private void drawSolvedClues() {
    List<Solved> solved = game.getLastSolved();
    for (int i = 0; i < solved.size(); i++) {
      pushMatrix();
      translate(50, 100 + ((i + 1) * 200f));
      String username = solved.get(i)
          .getTwitchuser() > 0
          ? game.getUserById(solved.get(i)
              .getTwitchuser())
          .getName()
          : "Nobody";
      drawSolvedClue(game.getClueById(solved.get(i)
          .getClue()), username, solved.get(i)
          .getScore());
      popMatrix();
    }
  }

  private void drawSolvedClue(Clue clue, String username, int score) {
    drawClue(clue, clue.getSolution(), true);
    String line = String.format("Solved by: %s (%d points)", username, score);
    fill(200);
    textSize(18);
    text(line, 0, 150);
  }

  private void drawScores() {
    List<Score> scores = game.getScores();
    scores.sort(Comparator.comparing(Score::getUserScore)
        .reversed());
    pushMatrix();
    fill(200);
    textSize(16f);
    textAlign(RIGHT);
    translate(1270, 115);
    text("Scores for " + Game.getScoreTypeBasedOnTime(), 0, 0);
    for (int i = 0; i < (Math.min(scores.size(), 15)); i++) {
      float x = 0;
      float y = (i + 1) * 25f;
      text(scores.get(i)
          .toString(), x, y);
    }
    popMatrix();
  }

  private void drawNextHintTime() {
    long nextHintTime = game.getTimeToNextHint();
    String type = game.clueFinished() ? "clue" : "hint";
    String timeRemaining = String.format("Next %s in %.1f seconds; worth %d points", type, (nextHintTime / 1000f), game.calcScore());
    textSize(NEXT_HINT_TIME_TEXT_SIZE);
    fill(NEXT_HINT_TIME_FILL);
    textAlign(LEFT, BOTTOM);
    text(timeRemaining, NEXT_HINT_TIME_X, NEXT_HINT_TIME_Y);
  }

  private void drawTiles(String currentAnswer, boolean clueFinished) {
    for (int i = 0; i < currentAnswer.length(); i++) {
      if (clueFinished) {
        fill(187, 220, 47);
      } else {
        fill(200);
      }
      rect((float) i * BOX_GAP, 0, BOX_SIZE, BOX_SIZE, 5);
      fill(50);
      textSize(ANSWER_TEXT_SIZE);

      char charToDraw = currentAnswer.charAt(i);
      textAlign(CENTER, CENTER);
      text(charToDraw, BOX_SIZE / 2f + (i * 50), (BOX_SIZE / 2f) - 4);
    }
  }
}