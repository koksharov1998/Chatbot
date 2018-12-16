import org.junit.jupiter.api.Test;
import telegram.Bot;

public class BotTest {

  private static String BOT_NAME = "JavaChatBot";
  private static String BOT_TOKEN = "746379722:AAFrKO3i2-xsRrGyXWXTp7pEH2s9WLqKM-s";
  @Test
  void shouldReturnGeneralHelp() {
    Bot bot = new Bot(BOT_NAME, BOT_TOKEN);
    bot.handle("1","/help");
  }
}
