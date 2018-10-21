import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Chatbot {

  public void start() throws FileNotFoundException {
    User user = new User("user");
    Scanner scanner = new Scanner(System.in);

    String s = "Question: How many bits in byte?\nAnswer: 8";
    InputStream stringInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

    File file = new File("quiz.txt");
    FileInputStream fileInputStream = new FileInputStream(file);

    QuizReader quizReader = new QuizReader(fileInputStream);
    Quiz quiz = new Quiz(quizReader);
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
