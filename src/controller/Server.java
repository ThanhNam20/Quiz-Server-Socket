package controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  public static ArrayList<Socket> listSocket;
  public Server() {}
  private void execute() throws IOException {
    int count = 0;
    ServerSocket serverSocket = new ServerSocket(Constant.SERVERPORT);
    System.out.println("Listening...");
    while(true){
      Socket socket = serverSocket.accept();
      System.out.println("Socket" + socket);
      count++;
      listSocket.add(socket);
      if(count == Constant.MIN_CLIENT_CONNECT){
        HandleClientConnect handleClientConnect = new HandleClientConnect(socket);
        handleClientConnect.start();
      }

    }
  }

  public static void main(String[] args) throws IOException {
    listSocket = new ArrayList<>();
    Server server = new Server();
    server.execute();
  }

}
