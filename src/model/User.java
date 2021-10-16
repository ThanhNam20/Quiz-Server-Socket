package model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
  private String userId;
  private String userName;
  private Integer userPoint;
  private String userRule;

  public User(String userName, Integer userPoint, String userRule) {
    this.userId = UUID.randomUUID().toString().replace("-", "");;
    this.userName = userName;
    this.userPoint = userPoint;
    this.userRule = userRule;

  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Integer getUserPoint() {
    return userPoint;
  }

  public void setUserPoint(Integer userPoint) {
    this.userPoint = userPoint;
  }

  public String getUserRule() {
    return userRule;
  }

  public void setUserRule(String userRule) {
    this.userRule = userRule;
  }
}
