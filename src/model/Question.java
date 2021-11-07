package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
  private Integer question_id;
  private String question_name;
  private Integer topic_id;

  public Question( Integer question_id ,String question_name, Integer topic_id) {
    this.question_id = question_id;
    this.question_name = question_name;
    this.topic_id = topic_id;
  }


  public Integer getAnswer_id() {
    return topic_id;
  }

  public void setAnswer_id(Integer answer_id) {
    this.topic_id = answer_id;
  }
  public Integer getQuestion_id() {
    return question_id;
  }

  public void setQuestion_id(Integer question_id) {
    this.question_id = question_id;
  }

  public String getQuestion_name() {
    return question_name;
  }

  public void setQuestion_name(String question_name) {
    this.question_name = question_name;
  }

}
