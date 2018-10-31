package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

  public static void main(String[] args) throws InterruptedException {

// запускаем подключение сокета по известным координатам и инициализируем приём сообщений с консоли клиента
    try (Socket socket = new Socket("localhost", 3345);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        DataInputStream ois = new DataInputStream(socket.getInputStream())) {

      System.out.println("Client connected to socket.");

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
          Thread thread1 = new Thread(() -> { //Тут лямбда выражение
            try {
              String a = ois.readUTF();
              System.out.println(a);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
          System.out.println("Client sent message");
          //String a = ois.readUTF();
          //System.out.println(a);
        }
      }
      System.out.println("Closing connections & channels on clientSide - DONE.");

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}