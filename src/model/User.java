package model;

import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class User implements Comparable<User> {
  private String userId;
  private String userName;
  private Integer userPoint;
  private Socket socket;
  private Integer roomId;

  public Integer getRoomId() {
    return roomId;
  }

  public void setRoomId(Integer roomId) {
    this.roomId = roomId;
  }

  public User(Socket socket) {
    this.userId = UUID.randomUUID().toString().replace("-", "");
    this.socket = socket;
    this.userPoint = 0;
  }

  public User(String userId, String userName) {
    this.userId = userId;
    this.userName = userName;
  }

  public User(String userName, Integer userPoint) {
    this.userPoint = userPoint;
    this.userName = userName;
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

  @Override
  public int compareTo(User o) {
    return this.userPoint.compareTo(o.userPoint);
  }
}
