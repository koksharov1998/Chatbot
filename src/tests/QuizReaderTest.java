import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import server.Pair;
import server.QuizReader;

public class QuizReaderTest {

  @Test
  void returnRightInputStreamAndWorkWithRightFile() {
    try {
      String answer = "Question: How many people go on the field from one football team?\nAnswer: 11";
      InputStream stringInputStream = new ByteArrayInputStream(
          answer.getBytes(StandardCharsets.UTF_8));
      QuizReader quizReader = new QuizReader(stringInputStream);
      Pair[] stream = quizReader.readFromStream();
      assertEquals(stream[0].getFirst(),
          "How many people go on the field from one football team?");
      assertEquals(stream[0].getSecond(),
          "11");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void returnRightInputStreamAndWorkWithWrongFile() {
    String answer = "How many people go on the field from one football team?\n11";
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    try {
      QuizReader quizReader = new QuizReader(stringInputStream);
      Pair[] stream = quizReader.readFromStream();
      stream[0].getFirst();
    } catch (Exception ex) {
      assertEquals(ex.getMessage().toString(), "File format is wrong");
    }
  }
}
