package ssta.xwdgame.game;

import ssta.xwdgame.entity.User;

public class Score {
  private final int clueCount;
  private final int userScore;
  private User user;

  public Score(User user, int clueCount, int score) {
    this.user = user;
    this.clueCount = clueCount;
    this.userScore = score;
  }

  @Override
  public String toString() {
    String name = getUser().getName();
    float average = ((float) getUserScore() / getClueCount());
    return String.format("%s --> %d/%d=%.2f", name, getUserScore(), getClueCount(), average);
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getUserScore() {
    return userScore;
  }

  public int getClueCount() {
    return clueCount;
  }
}
