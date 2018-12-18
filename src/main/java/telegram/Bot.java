package telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import server.User;


public class Bot extends TelegramLongPollingBot {

  private String botName;
  private String token;
  private Map<String, User> users = new ConcurrentHashMap<>();
  private GeneralHandler generalHandler = new GeneralHandler();
  private WikiHandler wikiHandler = new WikiHandler();
  private QuizHandler quizHandler = new QuizHandler();

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

  private String[] handle(String chatId, String input) {
    String[] lines;
    switch (users.get(chatId).getStatus()) {
      case 1:
        lines = wikiHandler.handle(input, users.get(chatId));
        break;
      case 2:
        lines = quizHandler.handle(input, users.get(chatId));
        break;
      case 3:
        lines = quizHandler.handle(input, users.get(chatId));
        break;
      case 4:
        lines = quizHandler.handle(input, users.get(chatId));
        break;
      default:
        lines = generalHandler.handle(input, users.get(chatId));
    }
    return lines;
  }

  private synchronized void sendMsg(String chatId, String s) {
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

  private synchronized void setButtons(SendMessage sendMessage) {
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
        keyboard.add(getKeyboardButtons("/start"));
        keyboard.add(getKeyboardButtons("/repeat"));
        keyboard.add(getKeyboardButtons("/result"));
        keyboard.add(getKeyboardButtons("/quit"));
        break;
      case 3:
        keyboard.add(getKeyboardButtons("/first"));
        keyboard.add(getKeyboardButtons("/second"));
        break;
      case 4:
        keyboard.add(getKeyboardButtons("/start"));
        keyboard.add(getKeyboardButtons("/continue"));
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
