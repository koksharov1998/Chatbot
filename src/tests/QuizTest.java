import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import server.Quiz;
import server.QuizReader;
import telegram.User;


class QuizTest {

  private final String answer = "Question: How many people go on the field from one football team?\nAnswer: 11";

  private void createTestQuizFile() {
    try {
      FileWriter fw = new FileWriter("testQuiz.txt", false);
      fw.write(answer);
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
  void returnRightCurrentQuestionAndWorkWithFileInput() throws FileNotFoundException {
    createTestQuizFile();
    File file = new File("testQuiz.txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    QuizReader quizReader = new QuizReader(fileInputStream);
    Quiz quiz = new Quiz(quizReader);
    quiz.moveNextQuestion();
    assertEquals("How many people go on the field from one football team?",
        quiz.getCurrentQuestion());
    User user = new User("name", 0);
    assertFalse(quiz.checkAnswer(user, "10"));
    assertEquals(user.getScore(), 0);
    assertTrue(quiz.checkAnswer(user, "11"));
    assertEquals(user.getScore(), 1);
    deleteTestQuizFile();
  }

  @Test
  void returnRightCurrentQuestionAndWorkWithStringInput() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    quiz.moveNextQuestion();
    assertEquals("How many people go on the field from one football team?",
        quiz.getCurrentQuestion());
  }

  @Test
  void containsOnlyOneQuestion() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    assertTrue(quiz.moveNextQuestion());
    assertFalse(quiz.moveNextQuestion());
  }

  @Test
  void rightAnswerIsRight() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user", 0);
    quiz.moveNextQuestion();
    assertTrue(quiz.checkAnswer(user, "11"));
  }

  @Test
  void wrongAnswerIsWrong() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user", 0);
    quiz.moveNextQuestion();
    assertFalse(quiz.checkAnswer(user, "22"));
  }

  @Test
  void userShouldGetScore() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user", 0);
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "11");
    assertEquals(1, user.getScore());
  }

  @Test
  void userShouldNotGetScore() {
    InputStream stringInputStream = new ByteArrayInputStream(
        answer.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user", 0);
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "22");
    assertEquals(0, user.getScore());
  }
}