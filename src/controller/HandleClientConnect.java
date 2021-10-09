package controller;

import com.google.gson.Gson;
import model.Question;
import model.Topic;
import model.User;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HandleClientConnect implements Runnable  {
  private Socket socket;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> arrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private User user;

  public HandleClientConnect(Socket socket) throws IOException {
    this.socket = socket;
    dataInputStream = new DataInputStream(socket.getInputStream());
    dataOutputStream = new DataOutputStream(socket.getOutputStream());
    dbConnection = DBConnection.getInstance();

  }

  public void addUser (String userName) throws Exception {
    try {
      dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
      dbQuery = new DBQuery(dbConnection.getConnection());
      User user = new User(userName, 0, "student");
      String query = "";
      ResultSet rs = dbQuery.execQuery(query);
      rs.close();
//      this.sendDataRoom();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void sendDataRoom() throws Exception {
    ArrayList<Topic> arrayList = new ArrayList<>();
    dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
    dbQuery = new DBQuery(dbConnection.getConnection());
    String query = "select * from topic";
    ResultSet rs = dbQuery.execQuery(query);
    while(rs.next()) {
      arrayList.add(new Topic(rs.getInt("topic_id"), rs.getString("topic_name"),rs.getInt("topic_question_count")));
    }
    Gson gson = new Gson();
    String roomData = gson.toJson(arrayList);
    System.out.println(roomData);
    dataOutputStream.writeUTF(roomData);
    dataOutputStream.flush();
    rs.close();
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
//          this.addUser(userName);
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
