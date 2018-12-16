package telegram;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.Quiz;
import server.QuizReader;
import server.User;

public class QuizHandler implements Handler {

  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";
  private Map<User, Quiz> quizes = new ConcurrentHashMap<>();

  public String[] handle(String input, User user) {
    Quiz quiz = quizes.get(user);
    List<String> lines = new ArrayList<>();
    switch (input) {
      case "/start":
        File file = new File("quiz.txt");
        quiz = quizes.get(user);
        try {
          FileInputStream fileInputStream = new FileInputStream(file);
          QuizReader quizReader = new QuizReader(fileInputStream);
          quizes.put(user, new Quiz(quizReader));
          quiz = quizes.get(user);
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        lines.add("Hello, dear " + user.getName() + "!");
        lines.add(helpQuiz);
        lines.add("Now we can start quiz! Let's go!");
        user.setScore(0);
        quiz.moveNextQuestion();
        lines.add(quiz.getCurrentQuestion());
        break;
      case "/help":
        lines.add(helpQuiz);
        break;
      case "/quit":
        user.setStatus(0);
        lines.add("Your score: " + user.getScore());
        lines.add("Bye!");
        break;
      case "/result":
        lines.add("Your score: " + user.getScore());
        break;
      case "/repeat":
        lines.add(quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          lines.add("It's right!");
        } else {
          lines.add("It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          user.setStatus(0);
          lines.add("Your score: " + user.getScore());
          lines.add("Bye!");
        } else {
          lines.add(quiz.getCurrentQuestion());
        }
    }
    return lines.toArray(new String[0]);
  }
}
