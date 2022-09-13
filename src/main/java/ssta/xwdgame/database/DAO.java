package ssta.xwdgame.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ssta.xwdgame.entity.Clue;
import ssta.xwdgame.entity.Solved;
import ssta.xwdgame.entity.User;
import ssta.xwdgame.game.Game;
import ssta.xwdgame.game.Score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.management.timer.Timer.*;

public class DAO {
  EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
  ThreadLocal<EntityManager> entityManager = ThreadLocal.withInitial(() -> entityManagerFactory.createEntityManager());
  private List<Solved> solved;


  public List<Clue> getAllClues() {
    TypedQuery<Clue> fetchAll = entityManager.get()
        .createNamedQuery("Clue.fetchAll", Clue.class);
    return fetchAll.getResultList();
  }

  public void persistClue(Clue clue) {
    entityManager.get()
        .persist(clue);
  }

  public void persistSolved(Solved solved) {
    solved.setWhen(System.currentTimeMillis());
    Transaction txn = getSession().beginTransaction();
    entityManager.get()
        .persist(solved);
    txn.commit();
    getSolved().add(solved);
  }

  private Session getSession() {
    return entityManager.get()
        .unwrap(Session.class);
  }

  private List<Solved> getSolved() {
    if (this.solved == null) {
      this.solved = entityManager.get()
          .createNamedQuery("Solved.fetchAll", Solved.class)
          .getResultList();
    }
    return solved;
  }

  public Map<Integer, Score> getScores(Game.ScoreType scoreType) {
    getSolved();
    Map<Integer, Score> response = new HashMap<>();
    final long since = calcWhenFromScoreType(scoreType);
    Map<Integer, List<Solved>> scoreList = getSolved().stream()
        .filter(s -> s.getWhen() > since)
        .collect(Collectors.groupingBy(Solved::getTwitchuser, Collectors.toList()));
    for (Map.Entry<Integer, List<Solved>> entry : scoreList.entrySet()) {
      Score scoreForUser = createScoreForUser(entry);
      response.put(entry.getKey(), scoreForUser);
    }
    return response;
  }

  private long calcWhenFromScoreType(Game.ScoreType scoreType) {
    long since;
    switch (scoreType) {
      case HOUR:
        since = System.currentTimeMillis() - ONE_HOUR;
        break;
      case DAY:
        since = System.currentTimeMillis() - ONE_DAY;
        break;
      case WEEK:
        since = System.currentTimeMillis() - ONE_WEEK;
        break;
      case MONTH:
        since = System.currentTimeMillis() - (ONE_WEEK * 4);
        break;
      case ALL_TIME:
        since = 0;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + scoreType);
    }
    return since;
  }

  private Score createScoreForUser(Map.Entry<Integer, List<Solved>> entry) {
    User user = getUserById(entry.getKey());
    int count = entry.getValue()
        .size();
    int score = entry.getValue()
        .stream()
        .map(Solved::getScore)
        .reduce(0, Integer::sum);
    return new Score(user, count, score);
  }

  private User getUserById(long userId) {
    return getAllUsers().stream()
        .filter(u -> u.getId() == userId)
        .findAny()
        .orElse(null);
  }

  public List<User> getAllUsers() {
    TypedQuery<User> fetchAll = entityManager.get()
        .createNamedQuery("User.fetchAll", User.class);
    return fetchAll.getResultList();
  }

  public User findOrCreateUserWithName(String username) {
    User user;
    List<User> users = entityManager.get()
        .createNamedQuery("User.findUserByName", User.class)
        .setParameter("name", username)
        .getResultList();
    if (users == null || users.isEmpty()) {
      user = persistUser(username);
    } else {
      user = users.get(0);
    }
    return user;
  }

  public User persistUser(String username) {
    User user = new User();
    user.setName(username);
    EntityTransaction txn = getSession().beginTransaction();
    entityManager.get()
        .persist(user);
    txn.commit();
    return user;
  }

  public Clue getClueById(int clueId) {
    return entityManager.get().find(Clue.class, clueId);
  }
}
