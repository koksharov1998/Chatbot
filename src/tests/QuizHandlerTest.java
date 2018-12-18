import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import server.User;
import telegram.QuizHandler;

public class QuizHandlerTest {

  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";
  private static final String helpWithCreation = "Command list:\n/help -- shows command list\n/first -- run the first quiz\n/second -- run the second quiz";
  private static final String helpStartOrContinue = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/continue -- you can continue your old quiz";

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
    assertEquals("Now you should choose /first or /second quiz", lines[0]);
    assertEquals(helpWithCreation, lines[1]);
    assertEquals(2, lines.length);
    assertEquals(3, user.getStatus());
  }

  @Test
  void shouldRepeatQuestion() {
    User user = new User("User1", 1);
    user.setStatus(2);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/first", user);
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
    quizHandler.handle("/first", user);
    quizHandler.handle("8", user);
    String[] lines = quizHandler.handle("/result", user);
    assertEquals("Your score: 1", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldReturnHelpWithCreation() {
    User user = new User("User1", 1);
    user.setStatus(3);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpWithCreation, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(3, user.getStatus());
  }

  @Test
  void shouldReturnFirstQuiz() {
    User user = new User("User1", 1);
    user.setStatus(3);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/first", user);
    assertEquals("Hello, dear User1!", lines[0]);
    assertEquals(helpQuiz, lines[1]);
    assertEquals("Now we can start quiz! Let's go!", lines[2]);
    assertEquals("How many bits in byte?", lines[3]);
    assertEquals(4, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldReturnSecondQuiz() {
    User user = new User("User1", 1);
    user.setStatus(3);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/second", user);
    assertEquals("Hello, dear User1!", lines[0]);
    assertEquals(helpQuiz, lines[1]);
    assertEquals("Now we can start quiz! Let's go!", lines[2]);
    assertEquals("The first four digits of PI? (Separate them by \",\")", lines[3]);
    assertEquals(4, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldReturnHelpStartOrContinue() {
    User user = new User("User1", 1);
    user.setStatus(4);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpStartOrContinue, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(4, user.getStatus());
  }

  @Test
  void shouldGoToStartNewQuiz() {
    User user = new User("User1", 1);
    user.setStatus(4);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/start", user);
    assertEquals("Now you should choose /first or /second quiz", lines[0]);
    assertEquals(helpWithCreation, lines[1]);
    assertEquals(2, lines.length);
    assertEquals(3, user.getStatus());
  }
}
