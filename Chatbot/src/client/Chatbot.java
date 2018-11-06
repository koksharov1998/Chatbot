package client;

import java.util.Scanner;


public class Chatbot {

  private Client client = new Client();

  public void start() {
    Scanner scanner = new Scanner(System.in);

    Thread readingThread = new Thread(() -> {
      boolean isAlive = true;
      while (isAlive) {
        String input = client.read();
        if (input.equals("quit")) {
          isAlive = false;
        } else {
          System.out.println(input);
        }
      }
    });
    readingThread.start();

    String input = "";
    boolean loop = true;
    while (loop) {
      input = scanner.nextLine().toLowerCase();
      loop = handleInput(input);
    }
    scanner.close();
  }

  private boolean handleInput(String input) {
    switch (input) {
      case "quit":
        client.send(input);
        return false;
      default:
        return client.send(input);
    }
  }
}
