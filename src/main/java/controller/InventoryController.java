package controller;

import model.InventoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
