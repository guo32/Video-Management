package controller;

import model.ActorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ActorController {
    private final Connection CONNECTION;

    public ActorController(Connection connection) {
        CONNECTION = connection;
    }

    public HashMap<Integer, String> selectAll() {
        HashMap<Integer, String> map = new HashMap<>();
        String query = "select * from `actor`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int key = resultSet.getInt("actor_id");
                String value = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                map.put(key, value);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[actor] selectAll error");
            e.printStackTrace();
        }
        return map;
    }

    public ActorDTO selectById(int id) {
        ActorDTO actorDTO = null;
        String query = "select * from `actor` where `actor_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                actorDTO = new ActorDTO();
                actorDTO.setActorId(resultSet.getInt("actor_id"));
                actorDTO.setFirstName(resultSet.getString("first_name"));
                actorDTO.setLastName(resultSet.getString("last_name"));
            }
        } catch (SQLException e) {
            System.out.println("[actor] selectById error");
            e.printStackTrace();
        }
        return actorDTO;
    }

    public HashMap<Integer, String> selectByFirstName(String firstName) {
        HashMap<Integer, String> map = new HashMap<>();
        String query = "select * from `actor` where `first_name` like ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, "%" + firstName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int key = resultSet.getInt("actor_id");
                String value = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                map.put(key, value);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[actor] selectByFirstName error");
            e.printStackTrace();
        }
        return map;
    }
}
