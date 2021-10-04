package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  private String dbURL;
  private String user;
  private String password;

  public DBConnection(String dbURL, String user, String password) {
    this.dbURL = dbURL;
    this.user = user;
    this.password = password;
  }

  public void execute (){
    try{
      Connection connection = DriverManager.getConnection(dbURL, user, password);
      if(connection != null) {
        System.out.println("Connect successfully");
      }

    }catch(SQLException ex){
      System.out.println(ex);
    }
  }

}
