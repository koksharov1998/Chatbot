package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private DataOutputStream oos;
  private DataInputStream ois;


  public Client() {

    try {
      socket = new Socket("localhost", 3348);
      oos = new DataOutputStream(socket.getOutputStream());
      ois = new DataInputStream(socket.getInputStream());

      System.out.println("Client connected to socket.");

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean send(String string) {
    try {
      oos.writeUTF(string);
      oos.flush();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public String read() {
    try {
      String a = ois.readUTF();
      return a;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error client";
  }


  public void start() {
    Scanner scanner = new Scanner(System.in);

    Thread readingThread = new Thread(() -> {
      boolean isAlive = true;
      while (isAlive) {
        String input = read();
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
        send(input);
        return false;
      default:
        return send(input);
    }
  }
}