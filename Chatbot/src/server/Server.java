package server;

import commons.User;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

  private static ServerSocket server;
  private static BufferedReader br;
  private static DataOutputStream out;
  private static DataInputStream in;

  public static void main(String[] args) throws InterruptedException {
    try {
      /*List<Socket> clients = new ArrayList<Socket>();
      Thread threadAddingClients = new Thread(() -> { //Тут лямбда выражение
        try {
          Socket client = server.accept();
          clients.add(client);
          System.out.println("Connection accepted with " + client);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      threadAddingClients.start();
      for (int i = 1; i < 20; i++) {
        TimeUnit.SECONDS.sleep(1);
      Socket client = clients.get(0);
      }*/

      server = new ServerSocket(3345);
      Socket client = server.accept();
      System.out.println("Connection accepted with " + client);

      br = new BufferedReader(new InputStreamReader(System.in));
      out = new DataOutputStream(client.getOutputStream());
      in = new DataInputStream(client.getInputStream());
      User user = new User("Mr.Dedok");
      File file = new File("quiz.txt");
      FileInputStream fileInputStream = new FileInputStream(file);

      QuizReader quizReader = new QuizReader(fileInputStream);
      Quiz quiz = new Quiz(quizReader);
      quiz.moveNextQuestion();
      send(quiz.getCurrentQuestion());

/*
      Thread threadWrite = new Thread(() -> {
        try {
          while (true) {
            String input = br.readLine();
            System.out.println(input);
            out.writeUTF("Server send you new message: " + input);
            out.flush();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      threadWrite.start();*/
      while (!client.isClosed()) {
        System.out.println("Server reading from channel" + client.toString());
        String entry = in.readUTF();
        analiz(entry,user, quiz);

      }

      /*
// неблокирующий ввод вывод в самой джаве разные реализации брать новую.
      while (!client.isClosed()) {
        System.out.println("Server reading from channel" + client.toString());
        String entry = in.readUTF();

        System.out.println("READ from client message - " + entry);
        System.out.println("Server try writing to channel");

// инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову - quit
        if (entry.equalsIgnoreCase("quit")) {
          System.out.println("Client initialize connections suicide ...");
          out.writeUTF("Server reply - " + entry + " - OK");
          out.flush();
          break;
        }
        out.writeUTF("Server reply - " + entry + " - OK");
        System.out.println("Server Wrote message to client.");
// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
        out.flush();
      }

      System.out.println("Client disconnected");
      System.out.println("Closing connections & channels.");
*/
      in.close();
      out.close();
      client.close();
      System.out.println("Closing connections & channels - DONE.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void analiz(String input, User user, Quiz quiz){
    switch (input) {
      case "result":
        send("Your score: " + user.getScore());
        break;
      case "repeat":
        send(quiz.getCurrentQuestion());
        break;
      default:
        quiz.checkAnswer(user, input);
        if (!quiz.moveNextQuestion()) {
          send("конец");
        }
        send(quiz.getCurrentQuestion());
    }
  }

  public static void send(String string){
    try {
      out.writeUTF(string);
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String read(){
    try {
      String a = in.readUTF();
      return a;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error";
  }
}
