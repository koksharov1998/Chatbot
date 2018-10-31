package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends Thread {

  public static void main(String[] args) throws InterruptedException {
    try (ServerSocket server = new ServerSocket(3345)) {
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
      Socket client = server.accept();
      System.out.println("Connection accepted with " + client);

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      DataOutputStream out = new DataOutputStream(client.getOutputStream());
      DataInputStream in = new DataInputStream(client.getInputStream());

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
        Thread thread1 = new Thread(() -> { //Тут лямбда выражение
          try {
            String input = br.readLine();
            System.out.println(input);
            out.writeUTF("Server send you new message: " + input);
            out.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        //String input = br.readLine();
        out.writeUTF("Server reply - " + entry + " - OK");
        //out.writeUTF("Server send you new message: " + input);
        System.out.println("Server Wrote message to client.");

// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
        out.flush();
      }

      System.out.println("Client disconnected");
      System.out.println("Closing connections & channels.");

      in.close();
      out.close();
      client.close();
      System.out.println("Closing connections & channels - DONE.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
