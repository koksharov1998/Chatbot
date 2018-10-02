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
    String input = "";
    while (true) {
      if (!input.equals("result") && !input.equals("help") && !input.equals("repeat")) {
        if (!quiz.loadQuestionInOrder()){
          break;
        }
        System.out.println(quiz.getCurrentQuestion());
      }
      input = scanner.nextLine().toLowerCase();
      if (input.equals("quit")) {
        break;
      }
      if (input.equals("result")) {
        System.out.println("Your score: " + user.getScore());
        continue;
      }
      if (input.equals("repeat")) {
        System.out.println(quiz.getCurrentQuestion());
        continue;
      }
      if (input.equals("help")) {
        writeHelp();
        continue;
      }
      quiz.checkAnswer(user, input);
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
