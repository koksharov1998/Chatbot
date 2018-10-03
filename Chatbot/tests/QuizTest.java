import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class QuizTest {

  private void createTestQuizFile() {
    try {
      FileWriter fw = new FileWriter("testQuiz.txt", false);
      fw.write("Question: How many people go on the field from one football team?\nAnswer: 11");
      fw.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void returnRightCurrentQuestion() {
    createTestQuizFile();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    assertEquals("How many people go on the field from one football team?",
        quiz.getCurrentQuestion());
  }

  @Test
  void containsOnlyOneQuestion() {
    createTestQuizFile();
    Quiz quiz = new Quiz("testQuiz.txt");
    assertTrue(quiz.moveNextQuestion());
    assertFalse(quiz.moveNextQuestion());
  }

  @Test
  void rightAnswerIsRight() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    assertTrue(quiz.checkAnswer(user, "11"));
  }

  @Test
  void wrongAnswerIsWrong() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    assertFalse(quiz.checkAnswer(user, "22"));
  }
}