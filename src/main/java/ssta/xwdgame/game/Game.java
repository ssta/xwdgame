package ssta.xwdgame.game;

import org.apache.commons.lang3.StringUtils;
import ssta.xwdgame.database.DAO;
import ssta.xwdgame.entity.Clue;
import ssta.xwdgame.entity.Solved;
import ssta.xwdgame.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Objects.isNull;

public class Game {
  public static final int MAX_LAST_SOLVES = 2;
  private static final long HINT_DELAY_TIME = 30L * 1000L;
  private final Random r = new Random();
  private final DAO dao = new DAO();
  private final LinkedList<Solved> lastSolved = new LinkedList<>();
  private final Map<ScoreType, Map<Integer, Score>> scores = new HashMap<>();
  private long nextHintTime = System.currentTimeMillis() + HINT_DELAY_TIME;
  private List<Clue> clues;
  private Clue currentClue;
  private String currentAnswer;
  private List<User> users;

  public Game() {
    getUsers();
  }

  private List<User> getUsers() {
    if (users == null) {
      users = dao.getAllUsers();
    }
    return users;
  }

  public void guess(String guess, User user) {
    user = getOrCreateUser(user.getName());
    if (guessCorrect(guess)) {
      addToSolvedAndPersist(getCurrentClue(), user);
      newClue();
    }
  }

  private boolean guessCorrect(String guess) {
    String removedNonLetters = guess.replaceAll("\\W", "");
    removedNonLetters = removedNonLetters.toUpperCase();
    return StringUtils.equals(removedNonLetters, getCurrentClue().getSolution());
  }

  public void addToSolvedAndPersist(Clue clue, User user) {
    Solved solved = new Solved();
    solved.setClue(clue.getId());
    solved.setTwitchuser(user.getId());
    solved.setScore(calcScore());
    dao.persistSolved(solved);
    addToLastSolved(solved);
  }

  private int calcScore(String currentAnswer) {
    // count the unrevealed letters
    return (int) currentAnswer.chars()
        .filter(c -> c == ' ')
        .count();
  }

  public int calcScore() {
    return calcScore(currentAnswer);
  }

  private void addToLastSolved(Solved solved) {
    lastSolved.addFirst(solved);
    if (lastSolved.size() > MAX_LAST_SOLVES) {
      lastSolved.removeLast();
    }
  }

  public Clue getCurrentClue() {
    if (isNull(currentClue)) {
      newClue();
    }
    return currentClue;
  }

  private void newClue() {
    if (isNull(clues)) {
      clues = dao.getAllClues();
    }
    currentClue = getRandomClue();
    // throw away clues with digits in them
    while (currentClue.getClueText()
        .split("\\(")[0].matches(".*\\d{1,2}.*")) {
      currentClue = getRandomClue();
    }
    currentAnswer = StringUtils.repeat(' ', (int) getCurrentClue().getLength());
    calcNextHintTime();
    updateScores();
  }

  private void calcNextHintTime() {
    nextHintTime = System.currentTimeMillis() + HINT_DELAY_TIME;
  }

  private Clue getRandomClue() {
    Clue clue = clues.get(r.nextInt(clues.size()));
    if (clue == null) {
      return getRandomClue();
    } else {
      return clue;
    }
  }

  public long getTimeToNextHint() {
    return nextHintTime - System.currentTimeMillis();
  }

  public String getCurrentAnswer() {
    if (currentAnswer == null) {
      currentAnswer = StringUtils.repeat(' ', (int) getCurrentClue().getLength());
    }
    return currentAnswer;
  }

  public void update() {
    if (System.currentTimeMillis() > nextHintTime) {
      if (clueFinished()) {
        newClue();
      } else {
        revealRandomLetter();
        if (clueFinished()) {
          Solved solved = new Solved();
          solved.setClue(getCurrentClue().getId());
          solved.setTwitchuser(0);
          addToLastSolved(solved);
          newClue();
        } else {
          calcNextHintTime();
        }
      }
    }
  }

  public boolean clueFinished() {
    return calcUnrevealedLetters().isEmpty();
  }

  public void revealRandomLetter() {
    List<Integer> unrevealedLetters = calcUnrevealedLetters();
    if (!unrevealedLetters.isEmpty()) {
      Collections.shuffle(unrevealedLetters);
      int index = unrevealedLetters.get(0);
      revealLetterAt(index);
    }
  }

  private List<Integer> calcUnrevealedLetters() {
    List<Integer> unrevealedLetters = new ArrayList<>();
    for (int i = 0; i < getCurrentAnswer().length(); i++) {
      if (getCurrentAnswer().charAt(i) == ' ') {
        unrevealedLetters.add(i);
      }
    }
    return unrevealedLetters;
  }

  public void revealLetterAt(int index) {
    char[] chars = getCurrentAnswer().toCharArray();
    chars[index] = getCurrentClue().getSolution()
        .charAt(index);
    this.currentAnswer = new String(chars);
  }

  public List<Solved> getLastSolved() {
    return lastSolved;
  }

  public Clue getClueById(int clueId) {
    return dao.getClueById(clueId);
  }

  public User getOrCreateUser(String username) {
    User user = getUsers().stream()
        .filter(u -> username.equals(u.getName()))
        .findAny()
        .orElse(null);
    if (user == null) {
      user = dao.persistUser(username);
      users.add(user);
    }
    return user;
  }

  public List<Score> getScores() {
    ScoreType scoreType = getScoreTypeBasedOnTime();
    return getScores(scoreType);
  }

  public static ScoreType getScoreTypeBasedOnTime() {
    // switch score type every 15 seconds
    int i = (int) (((System.currentTimeMillis() / 1000) / 15) % 5);
    return ScoreType.values()[i];
  }

  public List<Score> getScores(ScoreType scoreType) {
    if (scores.isEmpty()) {
      updateScores();
    }
    return new ArrayList<>(scores.get(scoreType)
        .values());
  }

  public void updateScores() {
    for (ScoreType scoreType : ScoreType.values()) {
      scores.put(scoreType, dao.getScores(scoreType));
    }
  }

  public User getUserById(int userId) {
    return getUsers().stream()
        .filter(u -> u.getId() == userId)
        .findAny()
        .orElse(null);
  }

  public enum ScoreType {
    HOUR("last hour"),
    DAY("last day"),
    WEEK("last week"),
    MONTH("last month"),
    ALL_TIME("all time");

    private final String description;

    ScoreType(String description) {
      this.description = description;
    }

    @Override
    public String toString() {
      return description;
    }
  }
}
