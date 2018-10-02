import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
  }

  @Test
  void checkAnswer() {
  }
}