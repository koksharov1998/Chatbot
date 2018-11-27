package telegram;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

  private static final String help = "Command list:\n/help -- shows command list\n/startQuiz -- starts quiz\n/startwiki -- finds on Wikipedia";
  private static String botName;
  private static String token;

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
      handle(update.getMessage().getChatId().toString(), message);
    }
  }

  private void handle(String chatId, Message input) {
    String s = input.getText().toLowerCase();
    switch (s) {
      case "/help":
        sendMsg(chatId, help);
        break;
      case "/startquiz":
        sendMsg(chatId, "Hello, dear user! What is your name?");
        break;
      case "/startwiki":
        sendMsg(chatId, "What do you want to find on Wikipedia?");
        break;
      default:
        sendMsg(chatId, "Я тебя не понимаю. Попробуй команду /help");
    }
  }

  public synchronized void sendMsg(String chatId, String s) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
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

  public String getBotUsername() {
    return botName;
  }

  public String getBotToken() {
    return token;
  }
}
