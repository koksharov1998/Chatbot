package telegram;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

  private static final boolean IsProxy = true;
  private static String BOT_NAME = "JavaChatBot";
  private static String BOT_TOKEN = "746379722:AAFrKO3i2-xsRrGyXWXTp7pEH2s9WLqKM-s";
  private static String PROXY_HOST = "odinmillion-vpn.cloudapp.net";
  private static Integer PROXY_PORT = 31337;
  private static String PROXY_USER = "sockduser";
  private static String PROXY_PASSWORD = "fuck_rkn_2018";

  public static void main(String[] args) {
    try {

      Authenticator.setDefault(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
        }
      });

      ApiContextInitializer.init();
      TelegramBotsApi botsApi = new TelegramBotsApi();

      if (IsProxy) {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyHost(PROXY_HOST);
        botOptions.setProxyPort(PROXY_PORT);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        botsApi.registerBot(new Bot(BOT_NAME, BOT_TOKEN, botOptions));
      } else {
        botsApi.registerBot(new Bot(BOT_NAME, BOT_TOKEN));
      }

    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
