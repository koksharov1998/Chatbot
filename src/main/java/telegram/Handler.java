package telegram;

public interface Handler {

  String[] handle(String input, User user);

}
