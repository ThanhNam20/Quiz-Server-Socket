package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constant.Constant;
import constant.RequestCode;
import model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

public class HandleClientConnect implements Runnable  {
  private Socket socket;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> questionArrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private User currentUser;
  private UserList userList;
  private Gson gson;
  private ClientRequest clientRequest;
  RoomManager clientRoomManager;

  public HandleClientConnect(Socket socket, RoomManager roomManager) throws IOException {
    this.socket = socket;
    this.clientRoomManager = roomManager;
    dataInputStream = new DataInputStream(socket.getInputStream());
    dataOutputStream = new DataOutputStream(socket.getOutputStream());
    gson = new Gson();
    dbConnection = DBConnection.getInstance();
  }

  @Override
  public void run() {
    try {
      while(true){
        String jsonData = dataInputStream.readUTF();
        clientAction(jsonData);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
        dataInputStream.close();
        dataOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void clientAction(String jsonData) throws IOException {
    Type clientRequestObject = new TypeToken<ClientRequest>(){}.getType();
    clientRequest = gson.fromJson(jsonData,clientRequestObject);
    Integer clientCode = clientRequest.getCode();
    String data = clientRequest.getData();

    switch(clientCode){
      case(RequestCode.USER_JOIN_GAME):
        String userName = data;
        handleUserJoinGame(userName);
        break;
      case (RequestCode.USER_JOIN_ROOM):
        String[] params = data.split(",");
        String userId = params[0];
        String roomId = params[1];
        handleUserJoinRoom(userId, roomId);
    }
  }

  public void handleUserJoinGame(String userName) throws IOException {
    currentUser = new User(userName, 0);
    System.out.println(currentUser.getUserId());
    clientRoomManager.addUserToGame(currentUser);
    dataOutputStream.writeUTF(gson.toJson(currentUser));
    dataOutputStream.flush();
    System.out.println("send user id: " + currentUser.getUserId() + " to client");
    sendDataRoom();
  }

  public void handleUserJoinRoom(String userId, String roomId) {
    System.out.println(userId + " join " + roomId);
    Topic selectedRoom = clientRoomManager.getRoomById(roomId);
    clientRoomManager.addUserToRoom(currentUser, selectedRoom);
  }

  public void sendDataRoom() throws IOException {
//    System.out.println(Server.listRooms);
    String roomData = gson.toJson(Server.listRooms);
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
  }

}

