package ssta.xwdgame.database;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ssta.xwdgame.entity.Clue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parser {

  private List<Clue> clues;

  @SuppressWarnings("java:S106") // I shouldn't use sout logging, but it's a one-off we'll be using this main method
  public static void main(String[] args) {
    // This is slow as fuck!  But we only have to do it once (or if we want to refresh the DB)
    // so I'm not going to worry about it!  Maybe consider bypassing hibernate and using
    // a batch update to do it if the slowness gets too bad!
    Parser parser = new Parser();
    @SuppressWarnings("java:S1075") // it's a once in a blue moon we'll run this!
    File dir = new File("/home/ssta/Java/xwdgame/src/main/resources/files");
    File[] files = dir.listFiles();
    parser.clues = new ArrayList<>();
    assert files != null;
    for (File file : files) {
      parser.clues.addAll(parser.parsePuzzle(file));
    }

    System.out.println("parser.getClues().size() = " + parser.getClues().size());
    DAO dao = new DAO();
    int count = 0;
    for (Clue clue : parser.getClues()) {
      if (count % 1000 == 0) {
        System.out.println(count);
      }
      if (clue != null) {
        dao.persistClue(clue);
      }
      count++;
    }
  }

  private Clue parseEntry(JSONObject json, String creator, String xwdName) {
    Clue entry = null;
    if (((JSONArray) json.get("group")).size() == 1) {
      entry = Clue.parseClue(json, creator, xwdName);
    }
    return entry;
  }

  public List<Clue> getClues() {
    if (this.clues == null) {
      readClues();
      return getClues();
    }
    return clues;
  }

  private void readClues() {
    clues = new ArrayList<>();
    File dir = new File("files/");
    if (!dir.exists()) {
      //noinspection ResultOfMethodCallIgnored
      dir.mkdirs();
    }
    for (File json : Objects.requireNonNull(dir.listFiles())) {
      clues.addAll(parsePuzzle(json));
    }
  }

  @SuppressWarnings("unchecked")
  private List<Clue> parsePuzzle(File file) {
    try {
      String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
      JSONObject json = (JSONObject) new JSONParser().parse(s);
      final String creator = getCreator(json);
      final String xwdName = (String) json.get("name");
      JSONArray entries = (JSONArray) json.get("entries");
      return (List<Clue>) entries.stream()
          .map(e -> parseEntry((JSONObject) e, creator, xwdName))
          .collect(Collectors.toList());
    } catch (IOException | ParseException e) {
      return Collections.emptyList();
    }
  }

  private String getCreator(JSONObject json) {
    return Optional.of(json)
        .map(j -> j.get("creator"))
        .map(JSONObject.class::cast)
        .map(j -> j.get("name"))
        .map(String.class::cast)
        .orElse("Unknown");
  }
}
