package model;

import java.io.Serializable;
import java.util.UUID;

public class Question implements Serializable{
  private String question_id;
  private String question_name;

  public Question( String question_name) {
    this.question_id = UUID.randomUUID().toString();
    this.question_name = question_name;
  }

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

}
