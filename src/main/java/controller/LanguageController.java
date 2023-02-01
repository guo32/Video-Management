package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LanguageController {
    private final Connection CONNECTION;

    public LanguageController(Connection connection) {
        CONNECTION = connection;
    }

    public HashMap<Integer, String> selectAll() {
        HashMap<Integer, String> map = new HashMap<>();
        String query = "select `language_id`, `name` from `language`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                map.put(resultSet.getInt("language_id"), resultSet.getString("name"));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[language] selectAll error");
            e.printStackTrace();
        }
        return map;
    }

    public boolean validateId(int id) {
        String query = "select * from `language` where `language_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[language] validateId error");
            e.printStackTrace();
        }
        return false;
    }
}
