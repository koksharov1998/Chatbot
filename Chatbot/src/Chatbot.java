import java.util.Scanner;

public class Chatbot {

  public void start() {
    User user = new User("user");
    Scanner scanner = new Scanner(System.in);
    Quiz quiz = new Quiz("quiz.txt");
    System.out
        .println("Hello, dear " + user.getName()
            + "!\nI'm java-chatbot. :)\nI can do some interesting things.");
    writeHelp();
    System.out.println("Now we can start quiz! Let's go!");
    quiz.moveNextQuestion();
    System.out.println(quiz.getCurrentQuestion());
    String input = "";
    boolean loop = true;
    while (loop) {
      input = scanner.nextLine().toLowerCase();
      loop = handleInput(input, quiz, user);
    }
    System.out.println("Your score: " + user.getScore());
    System.out.println("Bye!");
    scanner.close();
  }

  private boolean handleInput(String input, Quiz quiz, User user) {
    switch (input) {
      case "help":
        writeHelp();
        break;
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
        System.out.println(quiz.getCurrentQuestion());
    }
    return true;
  }

  private void writeHelp() {
    System.out.println(
        "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog");
  }
}
