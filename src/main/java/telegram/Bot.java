package telegram;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import server.Quiz;
import server.QuizReader;
import server.User;
import wiki.WikiApi;


public class Bot extends TelegramLongPollingBot {

  private static final String helpGeneral = "Command list:\n/help -- shows command list\n/quiz -- starts quiz\n/wiki -- finds on Wikipedia";
  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";
  private static final String helpWiki = "You can ask me something, and I will try to find about it a bit on Wikipedia!\n/help -- shows this message\n/quit -- you can choose something more interesting than searching on Wikipedia";
  private String botName;
  private String token;
  private Map<String, User> users = new HashMap<>();
  private Map<User, Quiz> quizes = new HashMap<>();
  private WikiApi wiki = new WikiApi();

  public Bot(String botName, String token, DefaultBotOptions options) {
    super(options);
    this.botName = botName;
    this.token = token;
  }

  public Bot(String botName, String token) {
    this.botName = botName;
    this.token = token;
  }

  public void onUpdateReceived(Update update) {
    Message message = update.getMessage();
    if (message != null && message.hasText()) {
      String chatId = message.getChatId().toString();
      if (!users.containsKey(chatId)) {
        users.put(chatId, new User(message.getChat().getFirstName(), Integer.parseInt(chatId)));
      }
      String[] lines = handle(chatId, message.getText().toLowerCase());
      for (String line : lines) {
        sendMsg(chatId, line);
      }
    }
  }

  public String[] handle(String chatId, String input) {
    String[] lines;
    switch (users.get(chatId).getStatus()) {
      case 1:
        lines = handleWiki(input, users.get(chatId));
        break;
      case 2:
        lines = handleQuiz(input, users.get(chatId));
        break;
      default:
        lines = handleGeneral(input, users.get(chatId));
    }
    return lines;
  }

  private String[] handleGeneral(String s, User user) {
    List<String> lines = new ArrayList<>();
    switch (s) {
      case "/start":
        lines.add("Hello, dear " + user.getName()
            + "!\nI'm java-chatbot. :)\nI can do some interesting things. We can find something on Wikipedia or play quiz!");
        lines.add(helpGeneral);
        break;
      case "/help":
        lines.add(helpGeneral);
        break;
      case "/wiki":
        user.setStatus(1);
        lines.add("What do you want to find on Wikipedia?");
        break;
      case "/quiz":
        user.setStatus(2);
        File file = new File("quiz.txt");
        FileInputStream fileInputStream = null;
        Quiz quiz = quizes.get(user);
        try {
          fileInputStream = new FileInputStream(file);
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
      default:
        lines.add("I don't understand you. Try to use command /help");
    }
    return lines.toArray(new String[0]);
  }

  private String[] handleWiki(String s, User user) {
    List<String> lines = new ArrayList<>();
    switch (s) {
      case "/help":
        lines.add(helpWiki);
        break;
      case "/quit":
        user.setStatus(0);
        lines.add("Let's try something else!");
        lines.add(helpGeneral);
        break;
      default:
        user.setStatus(0);
        lines.add(wiki.getWikiInformation(s));
        lines.add("If you want to find something, you need to repeat a command /startWiki");
    }
    return lines.toArray(new String[0]);
  }

  private String[] handleQuiz(String s, User user) {
    Quiz quiz = quizes.get(user);
    List<String> lines = new ArrayList<>();
    switch (s) {
      case "/help":
        lines.add(helpQuiz);
        break;
      case "/quit":
        user.setStatus(0);
        lines.add("Your score: " + user.getScore());
        lines.add("Bye!");
        lines.add(helpGeneral);
        break;
      case "/result":
        lines.add("Your score: " + user.getScore());
        break;
      case "/repeat":
        lines.add(quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, s)) {
          lines.add("It's right!");
        } else {
          lines.add("It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          user.setStatus(0);
          lines.add("Your score: " + user.getScore());
          lines.add("Bye!");
          lines.add(helpGeneral);
        } else {
          lines.add(quiz.getCurrentQuestion());
        }
    }
    return lines.toArray(new String[0]);
  }

  public synchronized void sendMsg(String chatId, String s) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableHtml(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(s);
    setButtons(sendMessage);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public synchronized void setButtons(SendMessage sendMessage) {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

    keyboard.add(getKeyboardButtons("/help"));
    switch (users.get(sendMessage.getChatId()).getStatus()) {
      case 0:
        keyboard.add(getKeyboardButtons("/quiz"));
        keyboard.add(getKeyboardButtons("/wiki"));
        break;
      case 1:
        keyboard.add(getKeyboardButtons("/quit"));
        break;
      case 2:
        keyboard.add(getKeyboardButtons("/repeat"));
        keyboard.add(getKeyboardButtons("/result"));
        keyboard.add(getKeyboardButtons("/quit"));
        break;
    }
    replyKeyboardMarkup.setKeyboard(keyboard);
  }

  private KeyboardRow getKeyboardButtons(String s) {
    KeyboardRow keyboardQuitRow = new KeyboardRow();
    keyboardQuitRow.add(new KeyboardButton(s));
    return keyboardQuitRow;
  }

  public String getBotUsername() {
    return botName;
  }

  public String getBotToken() {
    return token;
  }
}
