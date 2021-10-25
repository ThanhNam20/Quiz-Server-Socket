package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
  static DBConnection INSTANCE = null;
  Connection connection;
  String url = null, username = null, password = null;

  private DBConnection() {
  }

  public void connect(String host, String username, String password) throws Exception {
    this.url = host;
    this.username = username;
    this.password = password;
    this.connection = DriverManager.getConnection(this.url, this.username, this.password);
  }

  public Connection getConnection() {
//    System.out.println("Success!");
    return this.connection;
  }

  public static DBConnection getInstance() {
    if (DBConnection.INSTANCE == null) {
      DBConnection.INSTANCE = new DBConnection();
    }
    return DBConnection.INSTANCE;
  }

}
