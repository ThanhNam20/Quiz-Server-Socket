package model;

import controller.Constant;
import controller.DBConnection;
import controller.DBQuery;

import java.sql.ResultSet;
import java.util.ArrayList;

public class RoomList {
  private ArrayList<Topic> roomArrayList;
  private DBConnection dbConnection;
  private DBQuery dbQuery;

  public RoomList() throws Exception {
    this.roomArrayList = new ArrayList<Topic>();
    dbConnection = DBConnection.getInstance();
    dbQuery = new DBQuery(dbConnection.getConnection());
    this.getRoomData();
  }

  public RoomList(ArrayList<Topic> roomArrayList) {
    this.roomArrayList = roomArrayList;
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
