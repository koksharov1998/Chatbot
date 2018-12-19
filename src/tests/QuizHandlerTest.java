import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import telegram.User;
import telegram.QuizHandler;
import telegram.UserStatus;

public class QuizHandlerTest {

  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";
  private static final String helpWithCreation = "Command list:\n/help -- shows command list\n/first -- run the first quiz\n/second -- run the second quiz";
  private static final String helpStartOrContinue = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/continue -- you can continue your old quiz";

  @Test
  void shouldReturnQuizHelp() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpQuiz, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldReturnFromQuizToGeneral() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/quit", user);
    assertEquals("Your score: 0", lines[0]);
    assertEquals("Bye!", lines[1]);
    assertEquals(2, lines.length);
    assertEquals(UserStatus.Default, user.getStatus());
  }

  @Test
  void shouldStartNewQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/start", user);
    assertEquals("Now you should choose /first or /second quiz", lines[0]);
    assertEquals(helpWithCreation, lines[1]);
    assertEquals(2, lines.length);
    assertEquals(UserStatus.QuizChoosing, user.getStatus());
  }

  @Test
  void shouldRepeatQuestion() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/first", user);
    String[] lines = quizHandler.handle("/repeat", user);
    assertEquals("How many bits in byte?", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldShowRightResultAfterRightAnswer() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/first", user);
    String[] lines = quizHandler.handle("8", user);
    assertEquals("It's right!", lines[0]);
    assertEquals(2, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
    lines = quizHandler.handle("/result", user);
    assertEquals("Your score: 1", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldShowRightResultAfterWrongAnswer() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/first", user);
    String[] lines = quizHandler.handle("7", user);
    assertEquals("It's wrong!", lines[0]);
    assertEquals(2, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
    lines = quizHandler.handle("/result", user);
    assertEquals("Your score: 0", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldEndTheQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/second", user);
    for (int i = 0; i < 4; i++)
      quizHandler.handle(" ", user);
    String[] lines = quizHandler.handle(" ", user);
    assertEquals("It's wrong!", lines[0]);
    assertEquals("Your score: 0", lines[1]);
    assertEquals("Bye!", lines[2]);
    assertEquals(3, lines.length);
    assertEquals(UserStatus.Default, user.getStatus());
  }

  @Test
  void shouldReturnHelpWithCreation() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizChoosing);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpWithCreation, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizChoosing, user.getStatus());
  }

  @Test
  void shouldReturnFirstQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizChoosing);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/first", user);
    assertEquals("Hello, dear User1!", lines[0]);
    assertEquals(helpQuiz, lines[1]);
    assertEquals("Now we can start quiz! Let's go!", lines[2]);
    assertEquals("How many bits in byte?", lines[3]);
    assertEquals(4, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldReturnSecondQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizChoosing);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/second", user);
    assertEquals("Hello, dear User1!", lines[0]);
    assertEquals(helpQuiz, lines[1]);
    assertEquals("Now we can start quiz! Let's go!", lines[2]);
    assertEquals("The first four digits of PI? (Separate them by \",\")", lines[3]);
    assertEquals(4, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldReturnHelpStartOrContinue() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/help", user);
    assertEquals(helpStartOrContinue, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizStart, user.getStatus());
  }

  @Test
  void shouldGoToStartNewQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/start", user);
    assertEquals("Now you should choose /first or /second quiz", lines[0]);
    assertEquals(helpWithCreation, lines[1]);
    assertEquals(2, lines.length);
    assertEquals(UserStatus.QuizChoosing, user.getStatus());
  }

  @Test
  void shouldContinueQuiz() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    QuizHandler quizHandler = new QuizHandler();
    quizHandler.handle("/start", user);
    quizHandler.handle("/first", user);
    quizHandler.handle("8", user);
    quizHandler.handle("/quit", user);
    user.setStatus(UserStatus.QuizStart);
    String[] lines = quizHandler.handle("/continue", user);
    assertEquals(helpQuiz, lines[0]);
    assertEquals("Your score: 1", lines[1]);
    assertEquals("How many days in a leap year?", lines[2]);
    assertEquals(3, lines.length);
    assertEquals(UserStatus.QuizInGame, user.getStatus());
  }

  @Test
  void shouldDoNotContinueQuizAndStartNew() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("/continue", user);
    assertEquals("You dont't have old quiz", lines[0]);
    assertEquals("Now you should choose /first or /second quiz", lines[1]);
    assertEquals(helpWithCreation, lines[2]);
    assertEquals(3, lines.length);
    assertEquals(UserStatus.QuizChoosing, user.getStatus());
  }

  @Test
  void shouldDoNotUnderstandCommandInQuizChoosing() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizChoosing);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("abracadabra", user);
    assertEquals("I don't understand you. Try to use command /help", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizChoosing, user.getStatus());
  }

  @Test
  void shouldDoNotUnderstandCommandInQuizStart() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    QuizHandler quizHandler = new QuizHandler();
    String[] lines = quizHandler.handle("abracadabra", user);
    assertEquals("I don't understand you. Try to use command /help", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(UserStatus.QuizStart, user.getStatus());
  }
}
