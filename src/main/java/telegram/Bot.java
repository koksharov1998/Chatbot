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

  private static final String help = "Command list:\n/help -- shows command list\n/startQuiz -- starts quiz\n/startwiki -- finds on Wikipedia";
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
    if (users.get(chatId).getStatus() == 1) {
      if (s.equals("/help")) {
        sendMsgFromWiki(chatId, helpWiki);
        return;
      }
      if (s.equals("/quit")) {
        users.get(chatId).setStatus(0);
        sendMsg(chatId, "Let's try something else!");
        sendMsg(chatId, help);
        return;
      }
      String w = WikiApi.getWikiInformation(s);
      sendMsg(chatId, w);
      sendMsg(chatId, "If you want to find something, you need to repeat a command /startWiki");
      users.get(chatId).setStatus(0);
      return;
    }
    if (users.get(chatId).getStatus() == 2) {
      try {
        handleQuiz(s, users.get(chatId), quizes.get(users.get(chatId)), chatId);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
    switch (s) {
      case "/help":
        sendMsg(chatId, help);
        break;
      case "/startwiki":
        users.get(chatId).setStatus(1);
        sendMsgFromWiki(chatId, "What do you want to find on Wikipedia?");
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
        sendMsgFromQuiz(chatId, "Hello, dear user!");
        sendMsgFromQuiz(chatId, helpQuiz);
        sendMsgFromQuiz(chatId, "Now we can start quiz! Let's go!");
        users.get(chatId).setScore(0);
        quizes.get(users.get(chatId)).moveNextQuestion();
        sendMsgFromQuiz(chatId, quizes.get(users.get(chatId)).getCurrentQuestion());
        break;
      default:
        sendMsg(chatId, "I don't understand you. Try to use command /help");
    }
  }

  private void handleQuiz(String input, User user, Quiz quiz, String chatId) throws IOException {
    switch (input) {
      case "/help":
        sendMsgFromQuiz(chatId, helpQuiz);
        break;
      case "/quit":
        sendMsg(chatId, "Your score: " + user.getScore());
        sendMsg(chatId, "Bye!");
        user.setStatus(0);
        break;
      case "/result":
        sendMsgFromQuiz(chatId, "Your score: " + user.getScore());
        break;
      case "/repeat":
        sendMsgFromQuiz(chatId, quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          sendMsgFromQuiz(chatId, "It's right!");
        } else {
          sendMsgFromQuiz(chatId, "It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          sendMsg(chatId, "Your score: " + user.getScore());
          sendMsg(chatId, "Bye!");
          user.setStatus(0);
        } else {
          sendMsgFromQuiz(chatId, quiz.getCurrentQuestion());
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

  public synchronized void sendMsgFromQuiz(String chatId, String s) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableHtml(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(s);
    setButtonsForQuiz(sendMessage);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public synchronized void sendMsgFromWiki(String chatId, String s) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableHtml(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(s);
    setButtonsForWiki(sendMessage);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public synchronized void setButtons(SendMessage sendMessage) {
    // Создаем клавиуатуру
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    // Создаем список строк клавиатуры
    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

    // Первая строчка клавиатуры
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    // Добавляем кнопки в первую строчку клавиатуры
    keyboardFirstRow.add(new KeyboardButton("/help"));

    // Вторая строчка клавиатуры
    KeyboardRow keyboardSecondRow = new KeyboardRow();
    // Добавляем кнопки во вторую строчку клавиатуры
    keyboardSecondRow.add(new KeyboardButton("/startQuiz"));

    // Третья строчка клавиатуры
    KeyboardRow keyboardThirdRow = new KeyboardRow();
    // Добавляем кнопки во вторую строчку клавиатуры
    keyboardThirdRow.add(new KeyboardButton("/startWiki"));

    // Добавляем все строчки клавиатуры в список
    keyboard.add(keyboardFirstRow);
    keyboard.add(keyboardSecondRow);
    keyboard.add(keyboardThirdRow);
    // и устанваливаем этот список нашей клавиатуре
    replyKeyboardMarkup.setKeyboard(keyboard);
  }

  public synchronized void setButtonsForQuiz(SendMessage sendMessage) {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

    KeyboardRow keyboardFirstRow = new KeyboardRow();
    keyboardFirstRow.add(new KeyboardButton("/help"));

    KeyboardRow keyboardSecondRow = new KeyboardRow();
    keyboardSecondRow.add(new KeyboardButton("/repeat"));

    KeyboardRow keyboardThirdRow = new KeyboardRow();
    keyboardThirdRow.add(new KeyboardButton("/result"));

    KeyboardRow keyboardFourthRow = new KeyboardRow();
    keyboardFourthRow.add(new KeyboardButton("/quit"));

    keyboard.add(keyboardFirstRow);
    keyboard.add(keyboardSecondRow);
    keyboard.add(keyboardThirdRow);
    keyboard.add(keyboardFourthRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
  }

  public synchronized void setButtonsForWiki(SendMessage sendMessage) {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

    KeyboardRow keyboardFirstRow = new KeyboardRow();
    keyboardFirstRow.add(new KeyboardButton("/help"));
    KeyboardRow keyboardSecondRow = new KeyboardRow();
    keyboardSecondRow.add(new KeyboardButton("/quit"));

    keyboard.add(keyboardFirstRow);
    keyboard.add(keyboardSecondRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
  }

  public String getBotUsername() {
    return botName;
  }

  public String getBotToken() {
    return token;
  }
}
