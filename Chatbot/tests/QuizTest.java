/*import static org.junit.jupiter.api.Assertions.assertEquals;
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

import commons.User;
import org.junit.jupiter.api.Test;
import server.Quiz;
import server.QuizReader;

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
  void returnRightCurrentQuestionAndWorkWithFileInput() throws FileNotFoundException {
    createTestQuizFile();
    File file = new File("testQuiz.txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    QuizReader quizReader = new QuizReader(fileInputStream);
    Quiz quiz = new Quiz(quizReader);
    quiz.moveNextQuestion();
    assertEquals("How many people go on the field from one football team?",
        quiz.getCurrentQuestion());
    deleteTestQuizFile();
  }

  @Test
  void returnRightCurrentQuestionAndWorkWithStringInput() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    quiz.moveNextQuestion();
    assertEquals("How many people go on the field from one football team?",
        quiz.getCurrentQuestion());
  }

  @Test
  void containsOnlyOneQuestion() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    assertTrue(quiz.moveNextQuestion());
    assertFalse(quiz.moveNextQuestion());
  }

  @Test
  void rightAnswerIsRight() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user");
    quiz.moveNextQuestion();
    assertTrue(quiz.checkAnswer(user, "11"));
  }

  @Test
  void wrongAnswerIsWrong() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user");
    quiz.moveNextQuestion();
    assertFalse(quiz.checkAnswer(user, "22"));
  }

  @Test
  void userShouldGetScore() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user");
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "11");
    assertEquals(1, user.getScore());
  }

  @Test
  void userShouldNotGetScore() {
    String s = "Question: How many people go on the field from one football team?\nAnswer: 11";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    QuizReader quizReader = new QuizReader(stringInputStream);
    Quiz quiz = new Quiz(quizReader);
    User user = new User("user");
    quiz.moveNextQuestion();
    quiz.checkAnswer(user, "22");
    assertEquals(0, user.getScore());
  }
}*/