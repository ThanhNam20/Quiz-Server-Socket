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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandleClientConnect implements Runnable  {
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
    currentUser = new User(socket);
    this.clientRoomManager = roomManager;
    dataInputStream = new DataInputStream(currentUser.getSocket().getInputStream());
    dataOutputStream = new DataOutputStream(currentUser.getSocket().getOutputStream());
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
        currentUser.getSocket().close();
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
        System.out.println("RoomId"+ roomId);
        handleUserJoinRoom(userId, roomId);
        break;
      case (RequestCode.USER_SUBMIT_ANSWER):
        String userAnswerId = params[0]; // is userId but same name with other case;
        String answerId = params[1];
        handleUserSubmitAnswer(userAnswerId, answerId);
        break;

      case (RequestCode.USER_REQUEST_RAKING_CHART):
        String roomIdRequestChart = data;
        handleUserSubmitRequestRankingChart(roomIdRequestChart);
        break;
    }
  }
  // Case 1
  public void handleUserJoinGame(String userName) throws IOException {
    currentUser.setUserName(userName);
    System.out.println(currentUser.getUserId());
    dataOutputStream.writeUTF(gson.toJson(new User(currentUser.getUserId(), currentUser.getUserName())));
    dataOutputStream.flush();
    clientRoomManager.addUserToGame(currentUser);
    System.out.println("send user id: " + currentUser.getUserId() + " to client");
    sendDataRoom();
  }

  public void sendDataRoom() throws IOException {
  // System.out.println(Server.listRooms);
    String roomData = gson.toJson(clientRoomManager.getRoomList());
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
  }
  // Case 2
  public void handleUserJoinRoom(String userId, String roomId) throws IOException, SQLException {
    System.out.println(userId + " join " + roomId);
    Room selectedRoom = clientRoomManager.getRoomById(roomId);
    int code = clientRoomManager.addUserToRoom(currentUser, selectedRoom);
    dataOutputStream.writeUTF(Integer.toString(code));
    dataOutputStream.flush();
    if (code == RequestCode.ROOM_START) {
      // tim list user trong room => send question
      clientRoomManager.sendQuestionAndAnswerToRoom(selectedRoom);
    }
    if(code == RequestCode.ROOM_WAIT) {
      // Số người trong phòng không đủ thì gửi số người đang trong phòng để cập nhật
       ArrayList<User> userArrayList = clientRoomManager.getUserInRoom(selectedRoom);
       String listUserInRoom = gson.toJson(userArrayList);
       dataOutputStream.writeUTF(listUserInRoom);
       dataOutputStream.flush();
    }
    if(code == RequestCode.ROOM_FULL) {
      // Phòng vượt quá số người thì gửi message cho user
      dataOutputStream.writeUTF(Constant.ERROR_MESSAGE);
      dataOutputStream.flush();
    }
  }

  // Case 3
  public void handleUserSubmitAnswer(String userId, String answerId) throws Exception {
    dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
    dbQuery = new DBQuery(dbConnection.getConnection());
    String query = "select * from answer where answer_id = "+ answerId;
    ResultSet rs = dbQuery.execQuery(query);
    Answer answer = new Answer();
    if(rs.next()){
      answer = new Answer(rs.getInt("answer_id"),rs.getString("answer_title"), rs.getInt("is_true"));
    }
    String answerIsTrue = null;
    if(answer.isIs_true() == 1){
      answerIsTrue = "true";
    }else {
      answerIsTrue = "false";
    }
    System.out.println(answerIsTrue);
    User user = clientRoomManager.getUserById(userId);
    if(answerIsTrue.equals("true")){
      user.setUserPoint(user.getUserPoint()+1);
    }
    dataOutputStream.writeUTF(answerIsTrue);
    System.out.println(user.getUserPoint());
    dataOutputStream.flush();
  }

  // Case 4
  public void handleUserSubmitRequestRankingChart( String topicId) throws IOException {
    Room room = clientRoomManager.getRoomById(topicId);
    List<User> listClientInRoom = clientRoomManager.getUserInRoom(room);
    Collections.sort(listClientInRoom);
    String listClientInRoomJson = gson.toJson(listClientInRoom);
    System.out.println(listClientInRoomJson);
    dataOutputStream.writeUTF(listClientInRoomJson);
    dataOutputStream.flush();
  }

}

