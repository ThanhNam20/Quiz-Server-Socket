package model;

import java.io.Serializable;
import java.util.UUID;

public class Answer {
  private int answer_id;
  private String answer_title;
  private int is_true;

  public Answer() {
  }

  public Answer(int answer_id, String answer_title, int is_true) {
    this.answer_id = answer_id;
    this.answer_title = answer_title;
    this.is_true = is_true;
  }

  public Answer(int answer_id, String answer_title) {
    this.answer_id = answer_id;
    this.answer_title = answer_title;
  }

  public int getAnswer_id() {
    return answer_id;
  }

  public void setAnswer_id(int answer_id) {
    this.answer_id = answer_id;
  }

  public String getAnswer_title() {
    return answer_title;
  }

  public void setAnswer_title(String answer_title) {
    this.answer_title = answer_title;
  }

  public int isIs_true() {
    return is_true;
  }

  public void setIs_true(int is_true) {
    this.is_true = is_true;
  }

}
