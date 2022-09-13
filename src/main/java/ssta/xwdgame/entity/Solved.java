package ssta.xwdgame.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

@Entity
@Table(name = "SOLVED")
@NamedQuery(name = "Solved.fetchAll", query = "SELECT s from Solved s")
public class Solved {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "ID", nullable = false)
  private int id;
  @Basic
  @Column(name = "CLUE", nullable = false)
  private int clue;
  @Basic
  @Column(name = "TWITCHUSER", nullable = false)
  private int twitchuser;
  @Basic
  @Column(name = "SOLVEDWHEN")
  private long when;

  @Column(name = "SCORE")
  private int score;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getClue() {
    return clue;
  }

  public void setClue(int clue) {
    this.clue = clue;
  }

  public int getTwitchuser() {
    return twitchuser;
  }

  public void setTwitchuser(int twitchuser) {
    this.twitchuser = twitchuser;
  }

  public long getWhen() {
    return when;
  }

  public void setWhen(long when) {
    this.when = when;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}

    if (o == null || getClass() != o.getClass()) {return false;}

    Solved solved = (Solved) o;

    return new EqualsBuilder().append(id, solved.id).append(clue, solved.clue).append(twitchuser, solved.twitchuser).append(when, solved.when).isEquals();
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clue, twitchuser, when, score);
  }
}
