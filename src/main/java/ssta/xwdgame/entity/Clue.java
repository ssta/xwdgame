package ssta.xwdgame.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.simple.JSONObject;

@Entity
@NamedQuery(name = "Clue.fetchAll", query = "SELECT c from Clue c")
public class Clue {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "ID", nullable = false)
  private int id;
  @Basic
  @Column(name = "CLUE", nullable = false, length = -1)
  private String clueText;
  @Basic
  @Column(name = "CLUE_ID", nullable = false, length = -1)
  private String clueId;
  @Basic
  @Column(name = "CLUE_NUMBER", nullable = false)
  private long clueNumber;
  @Basic
  @Column(name = "DIRECTION", nullable = false, length = -1)
  private String direction;
  @Basic
  @Column(name = "LENGTH", nullable = false)
  private long length;
  @Basic
  @Column(name = "SETTER", nullable = false, length = -1)
  private String setter;
  @Basic
  @Column(name = "SOLUTION", nullable = false, length = -1)
  private String solution;
  @Basic
  @Column(name = "XWD_NAME", nullable = false, length = -1)
  private String xwdName;

  @Transient
  private long lastSolved;

  @Transient
  private User solvedByUser;

  public static Clue parseClue(JSONObject json, String creator, String xwdName) {
    Clue clue = new Clue();
    clue.setSetter(creator);
    clue.setXwdName(xwdName);
    clue.setClueNumber((long) json.get("number"));
    clue.setSolution((String) json.get("solution"));
    clue.setClueText((String) json.get("clue"));
    clue.setDirection((String) json.get("direction"));
    clue.setLength((long) json.get("length"));
    clue.setClueId((String) json.get("id"));
    return clue;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getClueText() {
    return clueText;
  }

  public void setClueText(String clue) {
    this.clueText = clue;
  }

  public String getClueId() {
    return clueId;
  }

  public void setClueId(String clueId) {
    this.clueId = clueId;
  }

  public long getClueNumber() {
    return clueNumber;
  }

  public void setClueNumber(long clueNumber) {
    this.clueNumber = clueNumber;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public String getSetter() {
    return setter;
  }

  public void setSetter(String setter) {
    this.setter = setter;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public String getXwdName() {
    return xwdName;
  }

  public void setXwdName(String xwdName) {
    this.xwdName = xwdName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}

    if (o == null || getClass() != o.getClass()) {return false;}

    Clue clue = (Clue) o;

    return new EqualsBuilder().append(id, clue.id).append(clueNumber, clue.clueNumber).append(length, clue.length)
        .append(clueText, clue.clueText).append(clueId, clue.clueId).append(direction, clue.direction)
        .append(setter, clue.setter).append(solution, clue.solution).append(xwdName, clue.xwdName).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(clueText).append(clueId).append(clueNumber).append(direction)
        .append(length).append(setter).append(solution).append(xwdName).toHashCode();
  }

  public long getLastSolved() {
    return lastSolved;
  }

  public void setLastSolved(long lastSolved) {
    this.lastSolved = lastSolved;
  }

  public User getSolvedByUser() {
    return solvedByUser;
  }

  public void setSolvedByUser(User solvedByUser) {
    this.solvedByUser = solvedByUser;
  }
}

