package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

  private Socket socket;
  private BufferedReader br;
  private DataOutputStream oos;
  private DataInputStream ois;


  public Client() {

    try {
      socket = new Socket("localhost", 3345);
      br = new BufferedReader(new InputStreamReader(System.in));
      oos = new DataOutputStream(socket.getOutputStream());
      ois = new DataInputStream(socket.getInputStream());

      System.out.println("Client connected to socket.");

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
      return a;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "error client";
  }
}