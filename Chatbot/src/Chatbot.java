import java.util.Scanner;

public class Chatbot {

  public static void main(String[] args) {
    User user = new User();
    Scanner scanner = new Scanner(System.in);
    Quiz quiz = new Quiz("quiz.txt");

    System.out.println("Hello!");
    writeHelp();
    String answer = "";
    Pair pair = new Pair("", "");
    while (true) {
      if (!answer.equals("result") && !answer.equals("help")) {
        pair = quiz.returnQuestionsInOrder();
        System.out.println(pair.getFirst());
      }
      answer = scanner.nextLine().toLowerCase();
      if (answer.equals("quit")) {
        System.out.println("Your score: " + user.getScore());
        System.out.println("Bye!");
        break;
      }
      if (answer.equals("result")) {
        System.out.println("Your score: " + user.getScore());
        continue;
      }
      if (answer.equals("help")) {
        writeHelp();
        continue;
      }
      if (answer.equals(pair.getSecond().toLowerCase())) {
        user.upScore();
        System.out.println("It's right!");
      } else {
        System.out.println("It's wrong!");
      }
    }
    scanner.close();
  }

  private static void writeHelp() {
    System.out.println("\nhelp\nresult\nquit\n");
  }
}
