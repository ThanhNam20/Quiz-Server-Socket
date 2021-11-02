package controller;

import constant.Constant;
import model.Topic;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomManager {
    private static HashMap<Topic, List<User>> roomMap;
    private static HashMap<String, User> userMap;
    private DataOutputStream dataOutputStream;
    private ExecutorService pool = Executors.newFixedThreadPool(Constant.ROOM_NUMBER);
    public RoomManager(List<Topic> roomList) {
        roomMap = new HashMap<Topic, List<User>>();
       roomList.forEach(room -> {
           roomMap.put(room, new ArrayList<User>());
       });
       userMap = new HashMap<String, User>();
    }

    public void addUserToGame(User user) {
        userMap.put(user.getUserId(), user);
    }

    public User getUserById(String userId) {
        return userMap.get(userId);
    }


    public void addUserToRoom(User user, Topic topic) throws IOException, SQLException {
        DataOutputStream dataOutputStream = new DataOutputStream(user.getSocket().getOutputStream());
        List<User> topicUser = roomMap.get(topic);
        user.setRoomId(topic.getTopicId());
        topicUser.add(user);
        System.out.println("Add user " + user.getUserId() + " joined room " + topic.getTopicId());
        System.out.println("Room " + topic.getTopicId() + " has " + topicUser.size() + " users");
//        if(topicUser.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM){
//            dataOutputStream.writeUTF("0");
//            dataOutputStream.flush();
//            return;
//        }
//        dataOutputStream.writeUTF("1");
//        dataOutputStream.flush();
         handleRoomStart(topicUser, topic);
    }

    public void handleRoomStart(List<User> userArrayList, Topic topic) throws SQLException {
//        if(userArrayList.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM) return;
        HandleMultiChoiceThread handleMultiChoiceThread = new HandleMultiChoiceThread(userArrayList, topic);
        String test = handleMultiChoiceThread.getQuestionByTopic();
        System.out.println(test);
    }

    public void removeUser(User user, Topic topic) {
        List<User> topicUser = roomMap.get(topic);
        topicUser.remove(user);
    }

    public boolean isJoin(User user, Topic topic) {
        List<User> topicUsers = roomMap.get(topic);
        return topicUsers.contains(user);
    }

    public boolean isOpen(Topic topic){
        List<User> userList = roomMap.get(topic);
        return !userList.isEmpty();
    }

    public void closeRoom(Topic topic){
        roomMap.remove(topic);
    }

    public Topic getRoomById(String roomId) {
        for (Topic room : roomMap.keySet()) {
            if (room.getTopicId() == Integer.parseInt(roomId)) {
                return room;
            }
        }
        return null;
    }

    public List<User> getUserInRoom(Topic topic){
      List<User> userList = roomMap.get(topic);
      return userList;
    }

    


}
