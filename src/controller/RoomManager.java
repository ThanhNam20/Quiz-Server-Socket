package controller;

import constant.Constant;
import model.Room;
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
    private static HashMap<Room, List<User>> roomMap;
    private static HashMap<String, User> userMap;
    public RoomManager() {
//        getTopicData();
        userMap = new HashMap<>();
    }

    private void getTopicData() {
        roomMap = new HashMap<>();
        dbConnection = DBConnection.getInstance();
        dbQuery = new DBQuery(dbConnection.getConnection());
        String query = "select * from room";
        ResultSet rs = null;
        try {
            rs = dbQuery.execQuery(query);
            while(rs.next()) {
                Room room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_name"));
                roomMap.put(room, new ArrayList<>()); // <Topic, List<User>>
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

    public void addUserToRoom(User user, Room room) throws IOException, SQLException {
        DataOutputStream dataOutputStream = new DataOutputStream(user.getSocket().getOutputStream());
        List<User> topicUser = roomMap.get(room);
        user.setRoomId(room.getTopicId());
        topicUser.add(user);
        System.out.println("Add user " + user.getUserId() + " joined room " + room.getTopicId());
        System.out.println("Room " + room.getTopicId() + " has " + topicUser.size() + " users");
        if(topicUser.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM){
            dataOutputStream.writeUTF("0");
            dataOutputStream.flush();
            return;
        }
        dataOutputStream.writeUTF("1");
        dataOutputStream.flush();
        handleRoomStart(topicUser, room);
    }

    public void handleRoomStart(List<User> userArrayList, Room room) throws SQLException {
        if(userArrayList.size() < Constant.MAX_NUMBER_CLIENT_IN_ROOM) return;
        HandleMultiChoiceThread handleMultiChoiceThread = new HandleMultiChoiceThread(userArrayList, room);
        handleMultiChoiceThread.getQuestionByTopic();
    }

    public Room getRoomById(String roomId) {
        for (Room room : roomMap.keySet()) {
            if (room.getTopicId() == Integer.parseInt(roomId)) {
                return room;
            }
        }
        return null;
    }

    public List<User> getUserInRoom(Room room){
      List<User> userList = roomMap.get(room);
      return userList;
    }

    public List<Room> getTopicList() {
        List<Room> roomList = new ArrayList<>();
        roomList.addAll(roomMap.keySet());
        return roomList;
    }
}
