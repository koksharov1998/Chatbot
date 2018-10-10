import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizReader {

  private String fileName;

  public QuizReader(String fn) {
    fileName = fn;
  }

  public Pair[] readFromFile() {
    List<Pair> text = new ArrayList<Pair>();
    try {
      FileReader fr = new FileReader(fileName);
      Scanner scan = new Scanner(fr);
      while (scan.hasNextLine()) {
        text.add(new Pair(scan.nextLine().substring(10), scan.nextLine().substring(8)));
      }
      scan.close();
      fr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return text.toArray(new Pair[text.size()]);
  }
}
