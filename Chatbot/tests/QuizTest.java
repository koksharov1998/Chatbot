import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QuizTest {

  @Test
  void getCurrentQuestionTest() {
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.loadQuestionInOrder();
    String actual = quiz.getCurrentQuestion();
    assertEquals("How many bits in byte?", actual);
  }

  @Test
  void loadQuestionInOrder() {
    Quiz quiz = new Quiz("testQuiz.txt");
    assertTrue(quiz.loadQuestionInOrder());
    assertFalse(quiz.loadQuestionInOrder());
  }

  @Test
  void checkAnswerTest() {
    User user = new User();
    Quiz quiz = new Quiz("testQuiz.txt");
    quiz.loadQuestionInOrder();
    assertTrue(quiz.checkAnswer(user, "11"));
    assertFalse(quiz.checkAnswer(user, "22"));
  }
}