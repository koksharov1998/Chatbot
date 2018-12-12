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

  private static final String helpGeneral = "Command list:\n/help -- shows command list\n/startQuiz -- starts quiz\n/startwiki -- finds on Wikipedia";
  private static final String helpQuiz = "Command list:\n/help -- shows command list\n/repeat -- repeat last question\n/result -- shows your score\n/quit -- finishes our quiz";
  private static final String helpWiki = "You can ask me something, and I will try to find about it a bit on Wikipedia!\n/help -- shows this message\n/quit -- you can choose something more interesting than searching on Wikipedia";
  private static String botName;
  private static String token;
  private Map<String, User> users = new HashMap<>();
  private Map<User, Quiz> quizes = new HashMap<>();

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
      String chatId = update.getMessage().getChatId().toString();
      if (!users.containsKey(chatId)) {
        users.put(chatId, new User(chatId, users.size()));
      }
      handle(chatId, message);
    }
  }

  private void handle(String chatId, Message input) {
    String s = input.getText().toLowerCase();
    switch (users.get(chatId).getStatus()) {
      case 0:
        handleGeneral(s, users.get(chatId));
        break;
      case 1:
        handleWiki(s, users.get(chatId));
        break;
      case 2:
        handleQuiz(s, users.get(chatId));
        break;
    }
  }

  private void handleWiki(String s, User user) {
    String chatId = user.getName();
    if (s.equals("/help")) {
      sendMsg(chatId, helpWiki);
      return;
    }
    if (s.equals("/quit")) {
      users.get(chatId).setStatus(0);
      sendMsg(chatId, "Let's try something else!");
      sendMsg(chatId, helpGeneral);
      return;
    }
    users.get(chatId).setStatus(0);
    String w = WikiApi.getWikiInformation(s);
    sendMsg(chatId, w);
    sendMsg(chatId, "If you want to find something, you need to repeat a command /startWiki");
  }

  private void handleGeneral(String s, User user) {
    String chatId = user.getName();
    switch (s) {
      case "/help":
        sendMsg(chatId, helpGeneral);
        break;
      case "/startwiki":
        users.get(chatId).setStatus(1);
        sendMsg(chatId, "What do you want to find on Wikipedia?");
        break;
      case "/startquiz":
        users.get(chatId).setStatus(2);
        File file = new File("quiz.txt");
        FileInputStream fileInputStream = null;
        try {
          fileInputStream = new FileInputStream(file);
          QuizReader quizReader = new QuizReader(fileInputStream);
          quizes.put(users.get(chatId), new Quiz(quizReader));
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        sendMsg(chatId, "Hello, dear user!");
        sendMsg(chatId, helpQuiz);
        sendMsg(chatId, "Now we can start quiz! Let's go!");
        users.get(chatId).setScore(0);
        quizes.get(users.get(chatId)).moveNextQuestion();
        sendMsg(chatId, quizes.get(users.get(chatId)).getCurrentQuestion());
        break;
      default:
        sendMsg(chatId, "I don't understand you. Try to use command /help");

    }
  }

  private void handleQuiz(String input, User user) {
    String chatId = user.getName();
    Quiz quiz = quizes.get(user);
    switch (input) {
      case "/help":
        sendMsg(chatId, helpQuiz);
        break;
      case "/quit":
        user.setStatus(0);
        sendMsg(chatId, "Your score: " + user.getScore());
        sendMsg(chatId, "Bye!");
        break;
      case "/result":
        sendMsg(chatId, "Your score: " + user.getScore());
        break;
      case "/repeat":
        sendMsg(chatId, quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          sendMsg(chatId, "It's right!");
        } else {
          sendMsg(chatId, "It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          user.setStatus(0);
          sendMsg(chatId, "Your score: " + user.getScore());
          sendMsg(chatId, "Bye!");
        } else {
          sendMsg(chatId, quiz.getCurrentQuestion());
        }
    }
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
        keyboard.add(getKeyboardButtons("/startQuiz"));
        keyboard.add(getKeyboardButtons("/startWiki"));
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
