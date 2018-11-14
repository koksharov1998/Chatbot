package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  private static ServerSocket server;

  public static void main(String[] args) {
    List<Thread> clients = new ArrayList<Thread>();
    try {
      server = new ServerSocket(3348);
      while (true) {
        final Socket client = server.accept();
        Thread clientHandler = new Thread(new MyRunnable(client));
        clients.add(clientHandler);
        clientHandler.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
