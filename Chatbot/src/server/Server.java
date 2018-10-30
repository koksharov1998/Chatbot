package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread
{
  public static void main(String[] args) throws InterruptedException {
    try (ServerSocket server= new ServerSocket(3345)){
      Socket client = server.accept();
      System.out.println("Connection accepted.");

      DataOutputStream out = new DataOutputStream(client.getOutputStream());
      DataInputStream in = new DataInputStream(client.getInputStream());


      Scanner scanner = new Scanner(System.in);

// неблокирующий ввод вывод в самой джаве разные реализации брать новую.
      while(!client.isClosed()){
        System.out.println("Server reading from channel" + client.toString());
        String entry = in.readUTF();

        System.out.println("READ from client message - "+entry);
        System.out.println("Server try writing to channel");

// инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову       - quit
        if(entry.equalsIgnoreCase("quit")){
          System.out.println("Client initialize connections suicide ...");
          out.writeUTF("Server reply - "+entry + " - OK");
          out.flush();
          break;
        }
        String input = scanner.nextLine();
        out.writeUTF("Server reply - "+entry + " - OK");
        out.writeUTF("Server send you new message: " + input);
        System.out.println("Server Wrote message to client.");

// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
        out.flush();
      }

      scanner.close();


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
