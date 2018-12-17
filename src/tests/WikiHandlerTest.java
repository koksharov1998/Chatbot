import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import server.User;
import telegram.WikiHandler;

public class WikiHandlerTest {

  private static final String helpWiki = "You can ask me something, and I will try to find about it a bit on Wikipedia!\n/help -- shows this message\n/quit -- you can choose something more interesting than searching on Wikipedia";
  private WikiHandler wikiHandler = new WikiHandler();

  @Test
  void shouldReturnWikiHelp() {
    User user = new User("User1", 1);
    user.setStatus(1);
    String[] lines = wikiHandler.handle("/help", user);
    assertEquals(helpWiki, lines[0]);
    assertEquals(1, lines.length);
    assertEquals(1, user.getStatus());
  }

  @Test
  void shouldReturnFromWikiToGeneral() {
    User user = new User("User1", 1);
    user.setStatus(1);
    String[] lines = wikiHandler.handle("/quit", user);
    assertEquals("Let's try something else!", lines[0]);
    assertEquals(1, lines.length);
    assertEquals(0, user.getStatus());
  }

  @Test
  void shouldReturnFromWikiToGeneralWithAnswer() {
    User user = new User("User1", 1);
    user.setStatus(1);
    String[] lines = wikiHandler.handle("Something", user);
    assertEquals("If you want to find something, you need to repeat a command /startWiki",
        lines[1]);
    assertEquals(2, lines.length);
    assertEquals(0, user.getStatus());
  }
}
