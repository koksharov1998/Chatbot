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
    String answer = "";
    while (true) {
      if (!answer.equals("result") && !answer.equals("help")) {
        if (!quiz.loadQuestionInOrder()){
          break;
        }
        System.out.println(quiz.getCurrentQuestion());
      }
      answer = scanner.nextLine().toLowerCase();
      if (answer.equals("quit")) {
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
      quiz.checkAnswer(user, answer);
    }
    System.out.println("Your score: " + user.getScore());
    System.out.println("Bye!");
    scanner.close();
  }

  private static void writeHelp() {
    System.out.println(
        "Command list:\nhelp -- shows command list\nresult -- shows your score\nquit -- finishes our dialog");
  }
}
