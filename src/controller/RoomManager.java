package controller;

import model.Topic;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RoomManager {
    private static HashMap<Topic, List<User>> roomMap;
    private static HashMap<String, User> userMap;

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

    public void addUserToRoom(User user, Topic topic) {
        List<User> topicUser = roomMap.get(topic);
        topicUser.add(user);
        System.out.println("Add user " + user.getUserId() + " joined room " + topic.getTopicId());
        System.out.println("Room " + topic.getTopicId() + " has " + topicUser.size() + " users");
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


}
