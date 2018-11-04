package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

  private static Socket socket;
  private static BufferedReader br;
  private static DataOutputStream oos;
  private static DataInputStream ois;


  public Client() {

// запускаем подключение сокета по известным координатам и инициализируем приём сообщений с консоли клиента
    try {
      socket = new Socket("localhost", 3345);
      br = new BufferedReader(new InputStreamReader(System.in));
      oos = new DataOutputStream(socket.getOutputStream());
      ois = new DataInputStream(socket.getInputStream());

      System.out.println("Client connected to socket.");
/*
      Thread threadRead = new Thread(() -> {
        try {
          while (true) {
            String a = ois.readUTF();
            System.out.println(a);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      threadRead.start();

// проверяем живой ли канал и работаем если живой
      while (!socket.isOutputShutdown()) {

// ждём консоли клиента на предмет появления в ней данных
        if (br.ready()) {
          System.out.println("Client start writing in channel...");
          String clientCommand = br.readLine();

// пишем данные с консоли в канал сокета для сервера
          oos.writeUTF(clientCommand);
          oos.flush();
          System.out.println("Client sent message " + clientCommand + " to server.");

          if (clientCommand.equalsIgnoreCase("quit")) {
            System.out.println("Client kill connections");
            if (ois.read() > -1) {
              System.out.println("reading...");
              String in = ois.readUTF();
              System.out.println(in);
            }
            break;
          }
          System.out.println("Client sent message");
        }
      }
      System.out.println("Closing connections & channels on clientSide - DONE.");
*/
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void send(String string) {
    try {
      oos.writeUTF(string);
      oos.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String read() {
    try {
      String a = ois.readUTF();
      System.out.println(a);
      return a;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error";
  }
}