package model;

import constant.Constant;
import controller.DBConnection;
import controller.DBQuery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomList {
  private ArrayList<Topic> roomArrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;

  public RoomList() throws Exception {
    roomArrayList = new ArrayList<>();
    dbConnection = DBConnection.getInstance();
    dbQuery = new DBQuery(dbConnection.getConnection());
    this.getRoomData();
  }

  public void getRoomData() throws  Exception{
    dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
    dbQuery = new DBQuery(dbConnection.getConnection());
    String query = "select * from topic";
    ResultSet rs = dbQuery.execQuery(query);
    while(rs.next()) {
      roomArrayList.add(new Topic(rs.getInt("topic_id"), rs.getString("topic_name"),rs.getInt("topic_question_count")));
    }
    rs.close();
  }

  public ArrayList<Topic> getRoomArrayList() {
    return this.roomArrayList;
  }

}
