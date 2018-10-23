package client;

import java.io.FileNotFoundException;

public class Program {

  public static void main(String[] args) {
    Chatbot chatbot = new Chatbot();
    try {
      chatbot.start();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
