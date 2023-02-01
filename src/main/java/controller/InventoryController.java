package controller;

import model.InventoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryController {
    private final Connection CONNECTION;

    public InventoryController(Connection connection) {
        CONNECTION = connection;
    }

    public void insert(InventoryDTO inventoryDTO) {
        String query = "insert into `inventory`(`film_id`, `store_id`) values(?, ?)";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, inventoryDTO.getFilmId());
            preparedStatement.setInt(2, inventoryDTO.getStoreId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[inventory] insert error");
            e.printStackTrace();
        }
    }

    public InventoryDTO selectById(int id) {
        InventoryDTO inventoryDTO = null;
        String query = "select * from `inventory` where `inventory_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                inventoryDTO = new InventoryDTO();
                inventoryDTO.setInventoryId(resultSet.getInt("inventory_id"));
                inventoryDTO.setFilmId(resultSet.getInt("film_id"));
                inventoryDTO.setStoreId(resultSet.getInt("store_id"));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[inventory] selectById error");
            e.printStackTrace();
        }
        return inventoryDTO;
    }

    public boolean validateInventoryId(int id) {
        String query = "select `inventory`.`inventory_id` from `inventory` " +
                "inner join `rental` on `inventory`.`inventory_id` = `rental`.`inventory_id` " +
                "where `inventory`.`inventory_id` = ? and `return_date` is null";
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
            System.out.println("[inventory] validateInventoryId error");
            e.printStackTrace();
        }
        return false;
    }

    public void delete(int id) {
        String query = "delete from `inventory` where `inventory_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[inventory] delete error");
            e.printStackTrace();
        }
    }
}
