import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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

  private void deleteTestQuizFile() {
    try {
      File file = new File("testQuiz.txt");
      if (!file.delete()) {
        throw new Exception("File didn't delete");
      }
    } catch (Exception e) {
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
    deleteTestQuizFile();
  }

  @Test
  void containsOnlyOneQuestion() {
    createTestQuizFile();
    Quiz quiz = new Quiz("testQuiz.txt");
    assertTrue(quiz.moveNextQuestion());
    assertFalse(quiz.moveNextQuestion());
    deleteTestQuizFile();
  }

  @Test
  void rightAnswerIsRight() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    assertTrue(quiz.checkAnswer(user, "11"));
    deleteTestQuizFile();
  }

  @Test
  void wrongAnswerIsWrong() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    assertFalse(quiz.checkAnswer(user, "22"));
    deleteTestQuizFile();
  }

  @Test
  void userShouldGetScore() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "11");
    assertEquals(1, user.getScore());
    deleteTestQuizFile();
  }

  @Test
  void userShouldNotGetScore() {
    createTestQuizFile();
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "22");
    assertEquals(0, user.getScore());
    deleteTestQuizFile();
  }
}