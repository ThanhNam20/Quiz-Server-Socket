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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HandleClientConnect implements Runnable  {
  private Socket socket;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> questionArrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private User currentUser;
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

  public void clientAction(String jsonData) throws Exception {
    Type clientRequestObject = new TypeToken<ClientRequest>(){}.getType();
    clientRequest = gson.fromJson(jsonData,clientRequestObject);
    Integer clientCode = clientRequest.getCode();
    String data = clientRequest.getData();
    String[] params = data.split(",");
    switch(clientCode){
      case(RequestCode.USER_JOIN_GAME):
        String userName = data;
        handleUserJoinGame(userName);
        break;
      case (RequestCode.USER_JOIN_ROOM):
        String userId = params[0];
        String roomId = params[1];
        handleUserJoinRoom(userId, roomId);
        break;
      case (RequestCode.USER_SUBMIT_ANSWER):
        String userAnswerId = params[0]; // is userId but same name with other case;
        String answerId = params[1];
        handleUserSubmitAnswer(userAnswerId, answerId);
        break;

      case (RequestCode.USER_REQUEST_RAKING_CHART):
        String userIdRequestChart = params[0];
        String roomIdRequestChart = params[1];
        handleUserSubmitRequestRankingChart(userIdRequestChart, roomIdRequestChart);
        break;
    }
  }
  // Case 1
  public void handleUserJoinGame(String userName) throws IOException {
    currentUser = new User(userName, 0);
    System.out.println(currentUser.getUserId());
    dataOutputStream.writeUTF(gson.toJson(currentUser));
    dataOutputStream.flush();
    currentUser.setSocket(socket);
    clientRoomManager.addUserToGame(currentUser);
    System.out.println("send user id: " + currentUser.getUserId() + " to client");
    sendDataRoom();
  }

  public void sendDataRoom() throws IOException {
  // System.out.println(Server.listRooms);
    String roomData = gson.toJson(clientRoomManager.getTopicList());
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
  }
  // Case 2
  public void handleUserJoinRoom(String userId, String roomId) throws IOException {
    System.out.println(userId + " join " + roomId);
    Topic selectedRoom = clientRoomManager.getRoomById(roomId);
    clientRoomManager.addUserToRoom(currentUser, selectedRoom);
  }
  // Case 3
  public void handleUserSubmitAnswer(String userId, String answerId) throws Exception {
    dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
    dbQuery = new DBQuery(dbConnection.getConnection());
    String query = "select is_true from answer where answer_id ="+ answerId;
    ResultSet rs = dbQuery.execQuery(query);
    System.out.println(rs);
    User user = clientRoomManager.getUserById(userId);
    if(true){
      user.setUserPoint(user.getUserPoint()+1);
      return;
    }
    return;
  }

  // Case 4
  public void handleUserSubmitRequestRankingChart(String userId, String topicId){
    Topic topic = clientRoomManager.getRoomById(topicId);
    List<User> listClientInRoom = clientRoomManager.getUserInRoom(topic);

  }



}

