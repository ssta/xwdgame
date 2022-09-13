package ssta.xwdgame.main;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Downloader {
  public static void main(String[] args) throws IOException, InterruptedException {
    // change the start/stop puzzle numbers to get a different set of puzzles
    for (int num = 21620; num > 21000; num--) {
      try {
        // delay here because otherwise the Guardian website will think we're doing 
        // something evil and give us errors
        Thread.sleep(500);
        download(num);
      } catch (HttpStatusException e) {
        if (e.getStatusCode() != 404) {
          throw e;
        }
      }
    }
  }

  private static void download(int num) throws IOException {
    String url = String.format("https://www.theguardian.com/crosswords/cryptic/%d", num);
    File dir = new File("files");
    if (!dir.mkdirs()) {
      throw new IllegalStateException("Unable to create files directory");
    }
    Document document = Jsoup.connect(url).get();
    Elements elementsByClass = document.getElementsByClass("js-crossword");
    if (!elementsByClass.isEmpty()) {
      Element element = elementsByClass.get(0);
      Attributes attributes = element.attributes();
      String s = attributes.get("data-crossword-data");
      File f = new File(String.format("files/Puzzle%d.json", num));
      try (FileWriter fw = new FileWriter(f)) {
        fw.write(s);
      }
      Logger.getAnonymousLogger().log(Level.INFO, () -> String.format("wrote file: %s", f.getName()));
    }
  }

}

