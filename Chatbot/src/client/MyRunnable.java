package client;

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

  public MyRunnable(Server server, Socket client) {
    this.server = server;
    this.client = client;
  }

  public void run() {
    System.out.println("Connection accepted with " + client);

    DataOutputStream out = null;
    try {
      out = new DataOutputStream(client.getOutputStream());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    DataInputStream in = null;
    try {
      in = new DataInputStream(client.getInputStream());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    File file = new File("quiz.txt");
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }

    QuizReader quizReader = new QuizReader(fileInputStream);
    Quiz quiz = new Quiz(quizReader);

    server.send("Hello, dear user! What is your name?");
    String name = server.read();
    User user = new User(name);
    server.send(user.getName() + "!\nI'm java-chatbot. :)\nI can do some interesting things.");
    server.send(help);
    server.send("Now we can start quiz! Let's go!");

    quiz.moveNextQuestion();
    server.send(quiz.getCurrentQuestion());

    while (!client.isClosed()) {
      System.out.println("Server reading from channel" + client.toString());
      String entry = server.read();
      server.handle(entry, user, quiz);
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


}