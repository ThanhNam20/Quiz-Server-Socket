package controller;

import model.Question;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleClientConnect extends Thread  {
  private Socket socket;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> arrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;


  public HandleClientConnect(Socket socket){
    this.socket = socket;
  }

  private void loginClient(){
//    DBConnection dBConnection = new DBConnection(Constant.DBURL, Constant.USER, Constant.PASSWORD);
//    dBConnection.execute();
  }

  @Override
  public void run() {
    dbConnection = DBConnection.getInstance();
    try {
      dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      dbQuery = new DBQuery(dbConnection.getConnection());
      String query = "SELECT * FROM user;";
      ResultSet rs = dbQuery.execQuery(query);
      System.out.println(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      arrayList = new ArrayList<Question>();
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      System.out.println(arrayList);
      objectOutputStream.writeObject(arrayList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
