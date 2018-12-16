package telegram;

import java.util.ArrayList;
import java.util.List;
import server.User;
import wiki.WikiApi;

public class WikiHandler implements Handler {

  private static final String helpWiki = "You can ask me something, and I will try to find about it a bit on Wikipedia!\n/help -- shows this message\n/quit -- you can choose something more interesting than searching on Wikipedia";
  private WikiApi wiki = new WikiApi();

  public String[] handle(String input, User user) {
    List<String> lines = new ArrayList<>();
    switch (input) {
      case "/help":
        lines.add(helpWiki);
        break;
      case "/quit":
        user.setStatus(0);
        lines.add("Let's try something else!");
        break;
      default:
        user.setStatus(0);
        lines.add(wiki.getWikiInformation(input));
        lines.add("If you want to find something, you need to repeat a command /startWiki");
    }
    return lines.toArray(new String[0]);
  }
}
