package telegram;

import server.User;

public interface Handler {

  String[] handle(String input, User user);

}
