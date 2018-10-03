import java.util.Scanner;

public class Chatbot {

  public static void main(String[] args) {
    User user = new User();
    Scanner scanner = new Scanner(System.in);
    Quiz quiz = new Quiz("quiz.txt");
    System.out
        .println("Hello, dear user!\nI'm java-chatbot. :)\nI can do some interesting things.");
    writeHelp();
    System.out.println("Now we can start quiz! Let's go!");
    quiz.moveNextQuestion();
    System.out.println(quiz.getCurrentQuestion());
    String input = scanner.nextLine().toLowerCase();
    boolean loop = true;
    while (loop) {
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
          loop = false;
          continue;
        default:
          quiz.checkAnswer(user, input);
          if (!quiz.moveNextQuestion()) {
            loop = false;
            continue;
          }
          System.out.println(quiz.getCurrentQuestion());
      }
      input = scanner.nextLine().toLowerCase();
    }
    System.out.println("Your score: " + user.getScore());
    System.out.println("Bye!");
    scanner.close();
  }

  private static void writeHelp() {
    System.out.println(
        "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog");
  }
}
