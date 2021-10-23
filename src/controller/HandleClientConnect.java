package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleClientConnect implements Runnable  {
  private Socket socket;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> questionArrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private User user;
  private Gson gson;
  private ClientRequest clientRequest;
  private UserList userList;
  public HandleClientConnect(Socket socket) throws IOException {
    this.socket = socket;
    dataInputStream = new DataInputStream(socket.getInputStream());
    dataOutputStream = new DataOutputStream(socket.getOutputStream());
    clientRequest = new ClientRequest();
    gson = new Gson();
    dbConnection = DBConnection.getInstance();
  }

  @Override
  public void run() {
    try {
      while(true ){
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
    String clientAction = clientRequest.getClientAction();
    String[] params = clientAction.split(",");

    switch(clientCode){
      case(Constant.ADD_USER):
        String userName = params[0];
        this.addUser(userName, dataOutputStream);
        break;
      case (Constant.HANDLE_CLIENT_JOIN_ROOM):
        String userId = params[0];
        String roomId = params[1];
    }
  }

  public void addUser (String userName, DataOutputStream dataOutputStream) throws IOException {
    try {
//      dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
//      dbQuery = new DBQuery(dbConnection.getConnection());
      User user = new User(userName, 0, "student");
      // Send user data
      String userData = gson.toJson(user);
      dataOutputStream.writeUTF(userData);
      dataOutputStream.flush();

      // Send list users connect
      Server.userLists.addUser(user);
      System.out.println(Server.userLists.getUserArrayList());

//      System.out.println(userList.getUserArrayList());
//      String userDataArray = gson.toJson(userList.getUserArrayList());
//      dataOutputStream.writeUTF(userDataArray);
//      dataOutputStream.flush();

//      String query = "";
//      ResultSet rs = dbQuery.execQuery(query);
//      rs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void sendDataRoom() throws Exception {
    System.out.println(Server.roomLists.getRoomArrayList());
    String roomData = gson.toJson(Server.roomLists.getRoomArrayList());
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
  }

}

