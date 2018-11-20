package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private DataOutputStream oos;
  private DataInputStream ois;
  private boolean isAlive = true;

  public Client() {
    try {
      socket = new Socket("localhost", 3348);
      oos = new DataOutputStream(socket.getOutputStream());
      ois = new DataInputStream(socket.getInputStream());
      System.out.println("Client connected to socket.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void send(String string) throws IOException {
    oos.writeUTF(string);
    oos.flush();
  }

  private String read(){
    try {
      return ois.readUTF();
    }catch (IOException e){
      return "quit";
    }
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);
    Thread readingThread = new Thread(() -> {
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
    while (loop && isAlive) {
      input = scanner.nextLine().toLowerCase();
      loop = handleInput(input);
    }
    scanner.close();
  }

  private boolean handleInput(String input) {
    try {
      switch (input) {
        case "quit":
          send(input);
          return false;
        default:
          send(input);
          return true;
      }
    } catch (IOException e) {
      System.out.println("Client disconnected to socket."+socket.toString());
      return false;
    }
  }
}