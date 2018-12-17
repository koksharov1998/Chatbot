import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import server.User;
import telegram.GeneralHandler;

public class GeneralHandlerTest {

  private static final String helpGeneral = "Command list:\n/help -- shows command list\n/quiz -- starts quiz\n/wiki -- finds on Wikipedia";
  private GeneralHandler generalHandler = new GeneralHandler();

  @Test
  void shouldReturnGeneralHelp() {
    User user = new User("User1", 1);
    String[] lines = generalHandler.handle("/help", user);
    assertEquals(helpGeneral, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(0, user.getStatus());
  }

  @Test
  void shouldReturnGreeting() {
    User user = new User("User1", 1);
    String[] lines = generalHandler.handle("/start", user);
    assertEquals(
        "Hello, dear User1!\nI'm java-chatbot. :)\nI can do some interesting things. We can find something on Wikipedia or play quiz!",
        lines[0]);
    assertEquals(helpGeneral, lines[1]);
    assertEquals(2, lines.length);
    assertEquals(0, user.getStatus());
  }

  @Test
  void shouldGoToWiki() {
    User user = new User("User1", 1);
    String[] lines = generalHandler.handle("/wiki", user);
    assertEquals("What do you want to find on Wikipedia?", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(1, user.getStatus());
  }

  @Test
  void shouldGoToQuiz() {
    User user = new User("User1", 1);
    String[] lines = generalHandler.handle("/quiz", user);
    assertEquals("Write command \"/start\" to start a quiz", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(2, user.getStatus());
  }

  @Test
  void shouldDoNotUnderstandCommand() {
    User user = new User("User1", 1);
    String[] lines = generalHandler.handle("abracadabra", user);
    assertEquals("I don't understand you. Try to use command /help", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(0, user.getStatus());
  }
}
