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
    quiz.loadQuestionInOrder();
    System.out.println(quiz.getCurrentQuestion());
    String input = scanner.nextLine().toLowerCase();
    loop:
    while (true) {
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
          break loop;
        default:
          quiz.checkAnswer(user, input);
          if (!quiz.loadQuestionInOrder()) {
            break loop;
          }
          System.out.println(quiz.getCurrentQuestion());
          input = scanner.nextLine().toLowerCase();
          continue;
      }
      input = scanner.nextLine().toLowerCase();
    }
    System.out.println("Your score: " + user.getScore());
    System.out.println("Bye!");
    scanner.close();
  }

  private static void writeHelp() {
    System.out.println(
        "Command list:\nhelp -- shows command list\nresult -- shows your score\nquit -- finishes our dialog\nrepeat -- repeat last question");
  }
}
