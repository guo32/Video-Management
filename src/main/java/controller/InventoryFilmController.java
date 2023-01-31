package controller;

import model.InventoryFilmDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;

public class InventoryFilmController {
    private final Connection CONNECTION;

    public InventoryFilmController(Connection connection) {
        CONNECTION = connection;
    }

    public ArrayList<InventoryFilmDTO> selectByStore(int storeId) {
        ArrayList<InventoryFilmDTO> list = new ArrayList<>();
        String query = "select `inventory`.`inventory_id`, `inventory`.`store_id`, `title`, `description`, `release_year`, `language`.`name`, " +
                "`rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`, `special_features` from `inventory` " +
                "inner join `film` on `inventory`.`film_id` = `film`.`film_id` " +
                "inner join `language` on `film`.`language_id` = `language`.`language_id` " +
                "where `store_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, storeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InventoryFilmDTO inventoryFilmDTO = new InventoryFilmDTO();
                inventoryFilmDTO.setInventoryId(resultSet.getInt("inventory_id"));
                inventoryFilmDTO.setStoreId(resultSet.getInt("store_id"));
                inventoryFilmDTO.setTitle(resultSet.getString("title"));
                inventoryFilmDTO.setDescription(resultSet.getString("description"));
                inventoryFilmDTO.setReleaseYear(Year.of(resultSet.getInt("release_year")));
                inventoryFilmDTO.setLanguage(resultSet.getString("name"));
                inventoryFilmDTO.setRentalDuration(resultSet.getInt("rental_duration"));
                inventoryFilmDTO.setRentalRate(resultSet.getDouble("rental_rate"));
                inventoryFilmDTO.setLength(resultSet.getInt("length"));
                inventoryFilmDTO.setReplacementCost(resultSet.getDouble("replacement_cost"));
                inventoryFilmDTO.setRating(resultSet.getString("rating"));
                inventoryFilmDTO.setSpecialFeatures(resultSet.getString("special_features"));

                list.add(inventoryFilmDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[inventory and film] selectByStore error");
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<InventoryFilmDTO> selectByFilmTitle(String title, int storeId) {
        ArrayList<InventoryFilmDTO> list = new ArrayList<>();
        String query = "select `inventory`.`inventory_id`, `title`, `rental_rate` from `inventory` " +
                "inner join `film` on `inventory`.`film_id` = `film`.`film_id` " +
                "where `store_id` = ? and `title` like ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, storeId);
            preparedStatement.setString(2, "%" + title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InventoryFilmDTO inventoryFilmDTO = new InventoryFilmDTO();
                inventoryFilmDTO.setInventoryId(resultSet.getInt("inventory_id"));
                inventoryFilmDTO.setTitle(resultSet.getString("title"));
                inventoryFilmDTO.setRentalRate(resultSet.getDouble("rental_rate"));

                list.add(inventoryFilmDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[inventory and film] selectByFilmTitle error");
            e.printStackTrace();
        }

        return list;
    }

    public InventoryFilmDTO selectById(int id) {
        InventoryFilmDTO inventoryFilmDTO = null;
        String query = "select `inventory`.`inventory_id`, `inventory`.`store_id`, `title`, `description`, `release_year`, `language`.`name`, " +
                "`rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`, `special_features` from `inventory` " +
                "inner join `film` on `inventory`.`film_id` = `film`.`film_id` " +
                "inner join `language` on `film`.`language_id` = `language`.`language_id` " +
                "where `inventory_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                inventoryFilmDTO = new InventoryFilmDTO();
                inventoryFilmDTO.setInventoryId(resultSet.getInt("inventory_id"));
                inventoryFilmDTO.setStoreId(resultSet.getInt("store_id"));
                inventoryFilmDTO.setTitle(resultSet.getString("title"));
                inventoryFilmDTO.setDescription(resultSet.getString("description"));
                inventoryFilmDTO.setReleaseYear(Year.of(resultSet.getInt("release_year")));
                inventoryFilmDTO.setLanguage(resultSet.getString("name"));
                inventoryFilmDTO.setRentalDuration(resultSet.getInt("rental_duration"));
                inventoryFilmDTO.setRentalRate(resultSet.getDouble("rental_rate"));
                inventoryFilmDTO.setLength(resultSet.getInt("length"));
                inventoryFilmDTO.setReplacementCost(resultSet.getDouble("replacement_cost"));
                inventoryFilmDTO.setRating(resultSet.getString("rating"));
                inventoryFilmDTO.setSpecialFeatures(resultSet.getString("special_features"));
            }
        } catch (SQLException e) {
            System.out.println("[inventory and film] selectById error");
            e.printStackTrace();
        }
        return inventoryFilmDTO;
    }
}
