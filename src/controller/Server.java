package controller;

import model.RoomList;
import model.Topic;
import model.User;
import model.UserList;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  public static ArrayList<Socket> listSocket;
  public static RoomList roomLists;
  public static UserList userLists;
  private static ArrayList<HandleClientConnect> clientHandleArrayList ;
  private ExecutorService pool = Executors.newFixedThreadPool(10);

  public Server() {}
  private void execute() {
    try  {
      ServerSocket serverSocket = new ServerSocket(Constant.SERVERPORT);
      System.out.println("Listening...");
      while(!(serverSocket.isClosed())){
        Socket socket = serverSocket.accept();
        listSocket.add(socket);
        System.out.println(socket + "connected");
        HandleClientConnect handleClientConnect = new HandleClientConnect(socket);
        clientHandleArrayList.add(handleClientConnect);
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
    roomLists = new RoomList();
    clientHandleArrayList = new ArrayList<>();
    userLists = new UserList();
    Server server = new Server();
    server.execute();
  }
}
