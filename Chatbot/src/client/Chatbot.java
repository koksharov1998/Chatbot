package client;

import commons.User;
import java.util.Scanner;


public class Chatbot {

  private Client client;

  public void start() {
    User user = new User("user");
    client = new Client();
    Scanner scanner = new Scanner(System.in);
    System.out
        .println("Hello, dear " + user.getName()
            + "!\nI'm java-chatbot. :)\nI can do some interesting things.");
    writeHelp();
    System.out.println("Now we can start quiz! Let's go!");

    Thread thread = new Thread(() -> {
      while (true) {
        System.out.println(client.read());
      }
    });
    thread.start();


    String input = "";
    boolean loop = true;
    while (loop) {
      input = scanner.nextLine().toLowerCase();
      loop = handleInput(input, user);
    }
    System.out.println("Your score: " + user.getScore());
    System.out.println("Bye!");
    scanner.close();
  }

  private boolean handleInput(String input, User user) {

    switch (input) {
      default:
        client.send(input);
        break;

        /*
      case "result":
        System.out.println("Your score: " + user.getScore());
        break;
      case "repeat":
        System.out.println(quiz.getCurrentQuestion());
        break;
      case "quit":
        return false;
      default:
        quiz.checkAnswer(user, input);
        if (!quiz.moveNextQuestion()) {
          return false;
        }
        System.out.println(quiz.getCurrentQuestion());*/
    }
    return true;
  }

  private void writeHelp() {
    System.out.println(
        "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog");
  }
}
