package controller;

import constant.Constant;
import model.Topic;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomManager {
    private DBConnection dbConnection;
    private DBQuery dbQuery;
    private static HashMap<Topic, List<User>> roomMap;
    private static HashMap<String, User> userMap;
    public RoomManager() {
        getTopicData();
        userMap = new HashMap<>();
    }

    private void getTopicData() {
        roomMap = new HashMap<>();
        dbConnection = DBConnection.getInstance();
        dbQuery = new DBQuery(dbConnection.getConnection());
        String query = "select * from topic";
        ResultSet rs = null;
        try {
            rs = dbQuery.execQuery(query);
            while(rs.next()) {
                Topic topic = new Topic(
                    rs.getInt("topic_id"),
                    rs.getString("topic_name"),
                    rs.getInt("topic_question_count")
                );
                roomMap.put(topic, new ArrayList<>()); // <Topic, List<User>>
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if(topicUser.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM){
            dataOutputStream.writeUTF("0");
            dataOutputStream.flush();
            return;
        }
        dataOutputStream.writeUTF("1");
        dataOutputStream.flush();
        handleRoomStart(topicUser, topic);
    }

    public void handleRoomStart(List<User> userArrayList, Topic topic) throws SQLException {
        if(userArrayList.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM) return;
        HandleMultiChoiceThread handleMultiChoiceThread = new HandleMultiChoiceThread(userArrayList, topic);
        handleMultiChoiceThread.getQuestionByTopic();
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

    public List<Topic> getTopicList() {
        List<Topic> topicList = new ArrayList<>();
        topicList.addAll(roomMap.keySet());
        return topicList;
    }
}
