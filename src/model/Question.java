package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Question implements Serializable{
  private String question_id;
  private String question_name;

  public String getQuestion_id() {
    return question_id;
  }

  public void setQuestion_id(String question_id) {
    this.question_id = question_id;
  }

  public String getQuestion_name() {
    return question_name;
  }

  public void setQuestion_name(String question_name) {
    this.question_name = question_name;
  }

  public ArrayList<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(ArrayList<Answer> answers) {
    this.answers = answers;
  }

  private ArrayList<Answer> answers;
  public Question( String question_name, ArrayList<Answer> answers) {
    this.question_id = UUID.randomUUID().toString();
    this.question_name = question_name;
    this.answers = answers;
  }


}
