package controller;

import model.Question;
import model.Topic;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HandleMultiChoiceThread implements Runnable{
  private List<User> userArrayList;
  private Topic topic;
  private DBConnection dbConnection;
  private DBQuery dbQuery;
  private ArrayList<Question> questionArrayList;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  public HandleMultiChoiceThread(List<User> userArrayList, Topic topic) {
    dbConnection = DBConnection.getInstance();
    dbQuery = new DBQuery(dbConnection.getConnection());
    this.userArrayList = userArrayList;
    this.topic = topic;
    questionArrayList = new ArrayList<>();
  }

  public void getQuestionByTopic(){

  }

  public void getAnswerQuestion(){

  }



  @Override
  public void run() {

  }
}
