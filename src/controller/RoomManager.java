package controller;

import constant.Constant;
import constant.RequestCode;
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


    public RoomManager() throws Exception {
        dbConnection = DBConnection.getInstance();
        getTopicData();
        userMap = new HashMap<>();
    }

    private void getTopicData() throws Exception {
        roomMap = new HashMap<>();
        dbConnection.connect(Constant.DBURL, Constant.USER, Constant.PASSWORD);
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

    public int addUserToRoom(User user, Room room) throws IOException, SQLException {
        List<User> topicUser = roomMap.get(room);
        user.setRoomId(room.getRoomId());
        System.out.println("Add user " + user.getUserId() + " joined room " + room.getRoomId());
        System.out.println("Room " + room.getRoomId() + " has " + topicUser.size() + " users");
        if (topicUser.size() > Constant.MAX_NUMBER_CLIENT_IN_ROOM) {
            return RequestCode.ROOM_FULL;
        } else if (topicUser.size() == Constant.MAX_NUMBER_CLIENT_IN_ROOM - 1) {
            topicUser.add(user);
            return RequestCode.ROOM_START;
        } else {
            topicUser.add(user);
            return RequestCode.ROOM_WAIT;
        }
    }

    public Room getRoomById(String roomId) {
        for (Room room : roomMap.keySet()) {
            if (room.getRoomId() == Integer.parseInt(roomId)) {
                return room;
            }
        }
        return null;
    }

    public List<User> getUserInRoom(Room room){
      List<User> userList = roomMap.get(room);
      return userList;
    }

    public void sendQuestionAndAnswerToRoom(Room room) throws SQLException {
        List<User> userList = roomMap.get(room);
        HandleMultiChoiceThread handleMultiChoiceThread = new HandleMultiChoiceThread(room);
        String data = handleMultiChoiceThread.getQuestionByTopic();
        System.out.println(data);

        userList.forEach(user -> {
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(user.getSocket().getOutputStream());
                dos.writeUTF(data);
                dos.flush();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Room> getRoomList() {
        List<Room> roomList = new ArrayList<>();
        roomList.addAll(roomMap.keySet());
        return roomList;
    }
}
