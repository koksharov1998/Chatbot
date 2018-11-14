package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class MyRunnable implements Runnable {

  private static final String help = "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog";
  private Socket client;
  private int ID;
  private DataOutputStream out;
  private DataInputStream in;

  public MyRunnable(Socket client, int ID) {
    this.client = client;
    this.ID = ID;
  }

  public void run() {
    System.out.println("Connection accepted with " + client);
    try {
      out = new DataOutputStream(client.getOutputStream());
      in = new DataInputStream(client.getInputStream());
      File file = new File("quiz.txt");
      FileInputStream fileInputStream = new FileInputStream(file);
      QuizReader quizReader = new QuizReader(fileInputStream);
      Quiz quiz = new Quiz(quizReader);
      fileInputStream.close();
      send("Hello, dear user! What is your name?");
      String name = read();
      User user = new User(name, ID);
      send(user.getName() + "!\nI'm java-chatbot. :)\nI can do some interesting things.");
      send(help);
      send("Now we can start quiz! Let's go!");
      quiz.moveNextQuestion();
      send(quiz.getCurrentQuestion());
      while (!client.isClosed()) {
        System.out.println("Server reading from channel" + client.toString());
        String entry = read();
        handle(entry, user, quiz);
      }
      in.close();
      out.close();
      client.close();
      System.out.println("Closing connections & channels - DONE.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handle(String input, User user, Quiz quiz) throws IOException {
    switch (input) {
      case "help":
        send(help);
        break;
      case "quit":
        send("Your score: " + user.getScore());
        send("Bye!");
        send("quit");
        client.close();
        break;
      case "result":
        send("Your score: " + user.getScore());
        break;
      case "repeat":
        send(quiz.getCurrentQuestion());
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          send("It's right!");
        } else {
          send("It's wrong!");
        }
        if (!quiz.moveNextQuestion()) {
          send("Your score: " + user.getScore());
          send("Bye!");
          send("quit");
          client.close();
        }
        else {
          send(quiz.getCurrentQuestion());
        }
    }
  }

  private void send(String string) {
    try {
      out.writeUTF(string);
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String read() {
    try {
      return in.readUTF();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error server";
  }
}