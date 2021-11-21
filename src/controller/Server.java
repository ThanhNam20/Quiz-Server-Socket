package controller;

import com.google.gson.Gson;
import constant.Constant;
import model.Answer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  public static ArrayList<Socket> listSocket;
  private List<HandleClientConnect> list = new ArrayList<>();
  private ExecutorService pool = Executors.newFixedThreadPool(10);
  private RoomManager roomManager;

  public Server() {}
  private void execute() {
    try  {
      ServerSocket serverSocket = new ServerSocket(Constant.SERVERPORT);
      System.out.println("Listening...");
      roomManager = new RoomManager();
      while(!(serverSocket.isClosed())){
        Socket socket = serverSocket.accept();
        listSocket.add(socket);
        System.out.println(socket + "connected");
        HandleClientConnect handleClientConnect = new HandleClientConnect(socket, roomManager);
        list.add(handleClientConnect);
        pool.execute(handleClientConnect);
      }
      serverSocket.close();
    }catch (IOException e){
      System.out.println(e);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    listSocket = new ArrayList<>();
    Server server = new Server();
    server.execute();
  }
}
