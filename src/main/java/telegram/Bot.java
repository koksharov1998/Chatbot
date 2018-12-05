package telegram;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import server.Quiz;
import server.QuizReader;
import server.User;
import wiki.WikiApi;


public class Bot extends TelegramLongPollingBot {

  private static final String help = "Command list:\n/help -- shows command list\n/startQuiz -- starts quiz\n/startwiki -- finds on Wikipedia";
  private static final String helpQuiz = "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog";
  private static String botName;
  private static String token;
  private static int status = 0;
  private static Quiz quiz;
  private static User user;

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
    if (status == 1) {
      String w = WikiApi.getWikiInformation(s);
      sendMsg(chatId, w);
      sendMsg(chatId, "If you want to find something, you need to repeat a command /startWiki");
      status = 0;
      return;
    }
    switch (s) {
      case "/help":
        sendMsg(chatId, help);
        break;
      case "/startquiz":
        status = 2;
        File file = new File("quiz.txt");
        FileInputStream fileInputStream = null;
        try {
          fileInputStream = new FileInputStream(file);
          QuizReader quizReader = new QuizReader(fileInputStream);
          quiz = new Quiz(quizReader);
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        sendMsg(chatId, "Hello, dear user! What is your name?");
        break;
      case "/startwiki":
        status = 1;
        sendMsg(chatId, "What do you want to find on Wikipedia?");
        break;
      default:
        if (status == 2) {
          status = 3;
          user = new User(s, 1);
          sendMsg(chatId,
              user.getName() + "!\nI'm java-chatbot. :)\nI can do some interesting things.");
          sendMsg(chatId, helpQuiz);
          sendMsg(chatId, "Now we can start quiz! Let's go!");
          quiz.moveNextQuestion();
          sendMsg(chatId, quiz.getCurrentQuestion());
          break;
        }
        if (status == 3) {
          try {
            handle(s, user, quiz, chatId);
          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
        }
        sendMsg(chatId, "I don't understand you. Try to use command /help");
    }
  }

  private void handle(String input, User user, Quiz quiz, String chatId) throws IOException {
    switch (input) {
      case "help":
        sendMsg(chatId, helpQuiz);
        break;
      case "quit":
        sendMsg(chatId, "Your score: " + user.getScore());
        sendMsg(chatId, "Bye!");
        //sendMsg(chatId, "quit");
        status = 0;
        break;
      case "result":
        sendMsg(chatId, "Your score: " + user.getScore());
        break;
      case "repeat":
        sendMsg(chatId, quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          sendMsg(chatId, "It's right!");
        } else {
          sendMsg(chatId, "It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          sendMsg(chatId, "Your score: " + user.getScore());
          sendMsg(chatId, "Bye!");
          //sendMsg(chatId, "quit");
          status = 0;
        } else {
          sendMsg(chatId, quiz.getCurrentQuestion());
        }
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
