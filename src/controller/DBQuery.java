package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {

   Connection connection;

  public DBQuery(Connection connection) {
    this.connection = connection;
  }

  public ResultSet execQuery(String query) throws SQLException {
    Statement statement = this.connection.createStatement();
    return statement.executeQuery(query);
  }

  public int updateQuery(String query) throws SQLException {
    Statement statement = this.connection.createStatement();
    return statement.executeUpdate(query);
  }
}
