package controller;

import com.google.gson.Gson;
import model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HandleMultiChoiceThread {
  private List<User> userArrayList;
  private Room room;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private ArrayList<Question> questionArrayList;
  private ArrayList<Answer> answerArrayList;
  private ArrayList<ArrayList<Answer>> listAnswers;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private Gson gson;
  private ArrayList<Integer> listQuestionId;
  private String answerInRoom;
  private String questionInRoom;

  public String getAnswerInRoom() {
    return answerInRoom;
  }

  public String getQuestionInRoom() {
    return questionInRoom;
  }

  public HandleMultiChoiceThread(Room room) {
    dbConnection = DBConnection.getInstance();
    this.room = room;
    questionArrayList = new ArrayList<>();
    answerArrayList = new ArrayList<>();
    listAnswers = new ArrayList<ArrayList<Answer>>();
    gson = new Gson();
  }

  public void getQuestionByTopic() throws SQLException {
    dbQuery = new DBQuery(dbConnection.getConnection());
    String query = "select * from question where question.room_id ="+ room.getRoomId();
    ResultSet rs = dbQuery.execQuery(query);
    while(rs.next()) {
      questionArrayList.add(new Question(rs.getInt("question_id"), rs.getString("question_title"), rs.getInt("room_id")));
    }
    rs.close();
    listQuestionId = new ArrayList<>();
    for (Question question: questionArrayList) {
      listQuestionId.add(question.getQuestion_id());
    }
    answerInRoom = getAnswerQuestion(listQuestionId);
    questionInRoom = gson.toJson(questionArrayList);
  }


  String getAnswerQuestion(ArrayList<Integer> listQuestionId )throws SQLException{
    listQuestionId.forEach((item) -> {
      dbQuery = new DBQuery(dbConnection.getConnection());
      String query = "select * from answer where answer.question_id = "+ item ;
      System.out.println(query);
      try {
        ResultSet rs = dbQuery.execQuery(query);
        while(rs.next()){
          answerArrayList.add(new Answer(rs.getInt("answer_id"), rs.getString("answer_title")));
        }
        rs.close();
        listAnswers.add(answerArrayList);
        answerArrayList = new ArrayList<>();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    String answerInRoom = gson.toJson(listAnswers);
    return answerInRoom;
  }

}
