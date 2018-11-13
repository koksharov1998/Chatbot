package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.Quiz;
import server.QuizReader;
import server.Server;
import server.User;

public class MyRunnable implements Runnable {

  private static final String help = "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog";
  private Server server;
  private Socket client;

  public MyRunnable(Socket client) {
    this.server = server;
    this.client = client;
  }

  public void run() {
    System.out.println("Connection accepted with " + client);

    try {

    DataOutputStream out = new DataOutputStream(client.getOutputStream());
    DataInputStream in = new DataInputStream(client.getInputStream());
    File file = new File("quiz.txt");
    FileInputStream fileInputStream = new FileInputStream(file);

    QuizReader quizReader = new QuizReader(fileInputStream);
    Quiz quiz = new Quiz(quizReader);

    send("Hello, dear user! What is your name?", out);
    String name = read(in);
    User user = new User(name);
    send(user.getName() + "!\nI'm java-chatbot. :)\nI can do some interesting things.", out);
    send(help, out);
    send("Now we can start quiz! Let's go!", out);

    quiz.moveNextQuestion();
    send(quiz.getCurrentQuestion(), out);

    while (!client.isClosed()) {
      System.out.println("Server reading from channel" + client.toString());
      String entry = read(in);
      handle(entry, user, quiz, out);
    }

    try {
      in.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      out.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      client.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    System.out.println("Closing connections & channels - DONE.");
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }

  }

  public static void handle(String input, User user, Quiz quiz, DataOutputStream out) {
    switch (input) {
      case "help":
        send(help, out);
        break;
      case "quit":
        send("Your score: " + user.getScore(), out);
        send("Bye!", out);
        send("quit", out);
      case "result":
        send("Your score: " + user.getScore(), out);
        break;
      case "repeat":
        send(quiz.getCurrentQuestion(), out);
        break;
      default:
        if (quiz.checkAnswer(user, input)) {
          send("It's right!", out);
        } else {
          send("It's wrong!", out);
        }
        if (!quiz.moveNextQuestion()) {
          send("Your score: " + user.getScore(), out);
          send("Bye!", out);
          send("quit", out);
        }
        send(quiz.getCurrentQuestion(), out);
    }
  }

  public static void send(String string, DataOutputStream out) {
    try {
      out.writeUTF(string);
      out.flush();
    } catch (Exception e) {
      //e.printStackTrace();
    }
  }

  public static String read(DataInputStream in) {
    try {
      return in.readUTF();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error server";
  }

}