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
  private static final String helpWithCreation = "Command list:\n/help -- shows command list\n/first -- run the first quiz\n/second -- run the second quiz";
  private static final String helpStartOrContinue = "Command list:\n/help -- shows command list\n/start -- run new quiz\n/continue -- you can continue your old quiz";
  private Map<User, Quiz> quizes = new ConcurrentHashMap<>();

  public String[] handle(String input, User user) {
    List<String> lines = new ArrayList<>();
    switch (user.getStatus()) {
      case 2:
        Quiz quiz = quizes.get(user);
        switch (input) {
          case "/help":
            lines.add(helpQuiz);
            break;
          case "/start":
            user.setStatus(3);
            lines.add("Now you should choose /first or /second quiz");
            lines.add(helpWithCreation);
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
              quizes.remove(user);
              lines.add("Your score: " + user.getScore());
              lines.add("Bye!");
            } else {
              lines.add(quiz.getCurrentQuestion());
            }
        }
        break;
      case 4:
        switch (input) {
          case "/help":
            lines.add(helpStartOrContinue);
            break;
          case "/start":
            user.setStatus(3);
            lines.add("Now you should choose /first or /second quiz");
            lines.add(helpWithCreation);
            break;
          case "/continue":
            if (quizes.containsKey(user)) {
              user.setStatus(2);
              quiz = quizes.get(user);
              lines.add(helpQuiz);
              lines.add("Your score: " + user.getScore());
              lines.add(quiz.getCurrentQuestion());
            } else {
              user.setStatus(3);
              lines.add("You dont't have old quiz");
              lines.add("Now you should choose /first or /second quiz");
              lines.add(helpWithCreation);
            }
            break;
          default:
            lines.add("I don't understand you. Try to use command /help");
        }
        break;
      case 3:
        File file;
        switch (input) {
          case "/help":
            lines.add(helpWithCreation);
            break;
          case "/first":
            user.setStatus(2);
            file = new File("firstQuiz.txt");
            try {
              FileInputStream fileInputStream = new FileInputStream(file);
              QuizReader quizReader = new QuizReader(fileInputStream);
              quizes.put(user, new Quiz(quizReader));
              fileInputStream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
            user.setScore(0);
            quizes.get(user).moveNextQuestion();
            lines.add("Hello, dear " + user.getName() + "!");
            lines.add(helpQuiz);
            lines.add("Now we can start quiz! Let's go!");
            lines.add(quizes.get(user).getCurrentQuestion());
            break;
          case "/second":
            user.setStatus(2);
            file = new File("secondQuiz.txt");
            try {
              FileInputStream fileInputStream = new FileInputStream(file);
              QuizReader quizReader = new QuizReader(fileInputStream);
              quizes.put(user, new Quiz(quizReader));
              fileInputStream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
            user.setScore(0);
            quizes.get(user).moveNextQuestion();
            lines.add("Hello, dear " + user.getName() + "!");
            lines.add(helpQuiz);
            lines.add("Now we can start quiz! Let's go!");
            lines.add(quizes.get(user).getCurrentQuestion());
            break;
          default:
            lines.add("I don't understand you. Try to use command /help");
        }
    }
    return lines.toArray(new String[0]);
  }
}

