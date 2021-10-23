package controller;

import com.google.gson.Gson;
import model.Question;
import model.RoomList;
import model.User;
import model.UserList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
  private UserList userList;

  public HandleClientConnect(Socket socket) throws IOException {
    this.socket = socket;
    dataInputStream = new DataInputStream(socket.getInputStream());
    dataOutputStream = new DataOutputStream(socket.getOutputStream());
    dbConnection = DBConnection.getInstance();
  }

  public void addUser (String userName) throws Exception {
    try {
      userList = new UserList();
      Gson gson = new Gson();
      dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
      dbQuery = new DBQuery(dbConnection.getConnection());
      User user = new User(userName, 0, "student");
      // Send user data
      String userData = gson.toJson(user);
      dataOutputStream.writeUTF(userData);
      dataOutputStream.flush();
      // Send list user connect

      userList.addUser(user);
      System.out.println(userList.getUserArrayList());
      String userDataArray = gson.toJson(userList.getUserArrayList());
      dataOutputStream.writeUTF(userDataArray);
      dataOutputStream.flush();

//      String query = "";
//      ResultSet rs = dbQuery.execQuery(query);
//      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void sendDataRoom() throws Exception {
    System.out.println(Server.listRooms);
    Gson gson = new Gson();
    String roomData = gson.toJson(Server.listRooms);
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
  }

  @Override
  public void run() {
    try {
      while(!socket.isClosed()){
        String userName = dataInputStream.readUTF();
        System.out.println(userName);
        if(userName.contains("exit")){
          System.out.println(socket + "disconected");
          socket.close();
        }else {
          this.addUser(userName);
          this.sendDataRoom();
        }
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
}

