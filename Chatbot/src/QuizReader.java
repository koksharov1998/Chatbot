import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizReader {

  private String fileName;

  public QuizReader(String fileName) {
    this.fileName = fileName;
  }

  public Pair[] readFromFile() {
    List<Pair> text = new ArrayList<Pair>();
    try {
      FileReader fr = new FileReader(fileName);
      Scanner scan = new Scanner(fr);
      String questionPrefix = "Question: ";
      String answerPrefix = "Answer: ";
      String newAnswer = null;
      String newQuestion = null;
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        if (line.startsWith(questionPrefix)) {
          newQuestion = line.substring(questionPrefix.length());
          continue;
        } else if (line.startsWith(answerPrefix)) {
          newAnswer = line.substring(answerPrefix.length());
        } else {
          throw new Exception("File format is wrong");
        }
        if (newAnswer == null || newQuestion == null) {
          throw new Exception("File format is wrong");
        } else {
          text.add(new Pair(newQuestion, newAnswer));
          newAnswer = null;
          newQuestion = null;
        }
      }
      scan.close();
      fr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return text.toArray(new Pair[text.size()]);
  }
}
