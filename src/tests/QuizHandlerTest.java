import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import server.User;
import telegram.QuizHandler;

public class QuizHandlerTest {

  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";

  @Test
  void shouldReturnQuizHelp() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpQuiz, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldReturnFromQuizToGeneral() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/quit", user);
    assertEquals("Your score: 0", lines[0]);
    assertEquals("Bye!", lines[1]);
    assertEquals(2, lines.length);
    assertEquals(0, user.getStatus());
  }

  @Test
  void shouldStartNewQuiz() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/start", user);
    assertEquals("Hello, dear User1!", lines[0]);
    assertEquals(helpQuiz, lines[1]);
    assertEquals("Now we can start quiz! Let's go!", lines[2]);
    assertEquals(4, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldRepeatQuestion() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    String[] lines = quizHandler.handle("/repeat", user);
    assertEquals("How many bits in byte?", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldShowRightResult() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("8", user);
    String[] lines = quizHandler.handle("/result", user);
    assertEquals("Your score: 1", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(2, user.getStatus());
  }
}
