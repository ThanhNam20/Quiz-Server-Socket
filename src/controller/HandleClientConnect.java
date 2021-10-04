package controller;

import model.Question;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HandleClientConnect extends Thread{
  private Socket socket;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private DataInputStream dataInputStream;
  private DataOutputStream dataOutputStream;
  private ArrayList<Question> arrayList;

  public HandleClientConnect(Socket socket){
    this.socket = socket;
  }

  private void loginClient(){
//    DBConnection dBConnection = new DBConnection(Constant.DBURL, Constant.USER, Constant.PASSWORD);
//    dBConnection.execute();
// Fake data

  }

  @Override
  public void run() {
    try {
      arrayList = new ArrayList<Question>();
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      arrayList.add(new Question("Thanh Nam 1"));
      arrayList.add(new Question("Thanh Nam 2"));
      arrayList.add(new Question("Thanh Nam 3"));
      arrayList.add(new Question("Thanh Nam 4"));
      arrayList.add(new Question("Thanh Nam 5"));
      System.out.println(arrayList);
      objectOutputStream.writeObject(arrayList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
