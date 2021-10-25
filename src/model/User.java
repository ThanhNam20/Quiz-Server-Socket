package model;

import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class User implements Serializable {
  private String userId;
  private String userName;
  private Integer userPoint;
  private Socket socket;

  public User(String userName, Integer userPoint, Socket socket) {
    this.userId = UUID.randomUUID().toString().replace("-", "");
    this.userName = userName;
    this.userPoint = userPoint;
    this.socket = socket;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
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

}
