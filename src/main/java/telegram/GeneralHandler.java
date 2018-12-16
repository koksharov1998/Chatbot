package telegram;

import java.util.ArrayList;
import java.util.List;
import server.User;

public class GeneralHandler implements Handler {

  private static final String helpGeneral = "Command list:\n/help -- shows command list\n/quiz -- starts quiz\n/wiki -- finds on Wikipedia";

  public String[] handle(String s, User user) {
    List<String> lines = new ArrayList<>();
    switch (s) {
      case "/start":
        lines.add("Hello, dear " + user.getName()
            + "!\nI'm java-chatbot. :)\nI can do some interesting things. We can find something on Wikipedia or play quiz!");
        lines.add(helpGeneral);
        break;
      case "/help":
        lines.add(helpGeneral);
        break;
      case "/wiki":
        user.setStatus(1);
        lines.add("What do you want to find on Wikipedia?");
        break;
      case "/quiz":
        user.setStatus(2);
        lines.add("Write command \"/start\" to start a quiz");
        break;
      default:
        lines.add("I don't understand you. Try to use command /help");
    }
    return lines.toArray(new String[0]);
  }
}
