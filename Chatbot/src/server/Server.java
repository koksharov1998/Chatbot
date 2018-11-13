package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  private static final String help = "Command list:\nhelp -- shows command list\nrepeat -- repeat last question\nresult -- shows your score\nquit -- finishes our dialog";
  private static ServerSocket server;
  private static List<Socket> clients = new ArrayList<Socket>();
  //private static DataOutputStream out;
  //private static DataInputStream in;

  public static void main(String[] args) throws InterruptedException {
    try {
      server = new ServerSocket(3348);

      while (true) {
        final Socket client = server.accept();
        clients.add(client);
        System.out.println("Connection accepted with " + client);
        Thread comWithClient = new Thread(new MyRunnable(client));
        comWithClient.start();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

      /*Thread threadAddingClients = new Thread(() -> {
        while (true) {
          try {
            Socket client = server.accept();
            clients.add(client);
            System.out.println("Connection accepted with " + client);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      threadAddingClients.start();*/

/*
      Socket client = server.accept();
      System.out.println("Connection accepted with " + client);

      out = new DataOutputStream(client.getOutputStream());
      in = new DataInputStream(client.getInputStream());
      File file = new File("quiz.txt");
      FileInputStream fileInputStream = new FileInputStream(file);

      QuizReader quizReader = new QuizReader(fileInputStream);
      Quiz quiz = new Quiz(quizReader);

      send("Hello, dear user! What is your name?");
      String name = read();
      User user = new User(name);
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
*/

/*
  public static void handle(String input, User user, Quiz quiz) {
    switch (input) {
      case "help":
        send(help);
        break;
      case "quit":
        send("Your score: " + user.getScore());
        send("Bye!");
        send("quit");
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
        }
        send(quiz.getCurrentQuestion());
    }
  }
*/
  /*
  public static void send(String string) {
    try {
      out.writeUTF(string);
      out.flush();
    } catch (Exception e) {
      //e.printStackTrace();
    }
  }

  public static String read() {
    try {
      String a = in.readUTF();
      return a;
    } catch (IOException e) {
      //e.printStackTrace();
    }
    return "error server";
  }
  */
}
