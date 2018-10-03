//import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

  @Test
  void getCurrentQuestionTest() {
    Quiz quiz = new Quiz();
    quiz.loadQuestionInOrder();
    String actual = quiz.getCurrentQuestion();
    assertEquals("How many bits in byte?", actual);
  }

  @Test
  void loadQuestionInOrder() {
    Quiz quiz = new Quiz();
    assertTrue(quiz.loadQuestionInOrder());
    assertFalse(quiz.loadQuestionInOrder());
  }

  @Test
  void checkAnswerTest() {
    User user = new User();
    String answer = "8";
    Quiz quiz = new Quiz();
    quiz.loadQuestionInOrder();
    assertTrue(quiz.checkAnswer(user, answer));
  }
}