package model;

import java.util.ArrayList;

public class UserList {
  private ArrayList<User> userArrayList;

  public UserList() {
    userArrayList = new ArrayList<>();
  }

  public ArrayList<User> getUserArrayList() {
    return userArrayList;
  }

  public void addUser(User user){
    this.userArrayList.add(user);
  }

}
