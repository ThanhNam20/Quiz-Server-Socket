package model;

import java.util.ArrayList;

public class UserList {
  public ArrayList<User> userArrayList;

  public ArrayList<User> getUserArrayList() {
    return userArrayList;
  }

  public void setUserArrayList(ArrayList<User> userArrayList) {
    this.userArrayList = userArrayList;
  }

  public void addUser(User user){
    this.userArrayList.add(user);
  }

}
