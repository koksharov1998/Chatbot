import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizReader {

  private String fileName;

  public QuizReader(String fn) {
    fileName = fn;
  }

  public Pair[] readAndPrintFromFile() {
    ArrayList<Pair> text = new ArrayList<Pair>();
    try {
      FileReader fr = new FileReader(fileName);
      Scanner scan = new Scanner(fr);
      int i = 1;
      String first = "";
      String second = "";
      while (scan.hasNextLine()) {
        if (i % 2 == 1) {
          first = scan.nextLine().substring(10);
        } else {
          second = scan.nextLine().substring(8);
          text.add(new Pair(first, second));
        }
        i++;
      }
      scan.close();
      fr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
        /*for (Pair line : text) {
            System.out.println(line.getFirst());
            System.out.println(line.getSecond());
        }*/
    return text.toArray(new Pair[text.size()]);
  }
}
