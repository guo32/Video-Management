package controller;

import model.RentalDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalController {
    private final Connection CONNECTION;

    public RentalController(Connection connection) {
        CONNECTION = connection;
    }

    public ArrayList<RentalDTO> selectAll() {
        ArrayList<RentalDTO> list = new ArrayList<>();
        String query = "select * from `rental`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RentalDTO rentalDTO = new RentalDTO();
                rentalDTO.setRentalId(resultSet.getInt("rental_id"));
                rentalDTO.setRentalDate(resultSet.getTimestamp("rental_date"));
                rentalDTO.setInventoryId(resultSet.getInt("inventory_id"));
                rentalDTO.setCustomerId(resultSet.getInt("customer_id"));
                rentalDTO.setReturnDate(resultSet.getTimestamp("return_date"));
                rentalDTO.setStaffId(resultSet.getInt("staff_id"));

                list.add(rentalDTO);
            }
        } catch (SQLException e) {
            System.out.println("[rental] selectAll error");
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<RentalDTO> selectByCustomerId(int customerId, int staffId) {
        ArrayList<RentalDTO> list = new ArrayList<>();
        String query = "select * from `rental` where `customer_id` = ? and `staff_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, staffId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RentalDTO rentalDTO = new RentalDTO();
                rentalDTO.setRentalId(resultSet.getInt("rental_id"));
                rentalDTO.setRentalDate(resultSet.getTimestamp("rental_date"));
                rentalDTO.setInventoryId(resultSet.getInt("inventory_id"));
                rentalDTO.setCustomerId(resultSet.getInt("customer_id"));
                rentalDTO.setReturnDate(resultSet.getTimestamp("return_date"));
                rentalDTO.setStaffId(resultSet.getInt("staff_id"));

                list.add(rentalDTO);
            }
        } catch (SQLException e) {
            System.out.println("[rental] selectByCustomerId error");
            e.printStackTrace();
        }
        return list;
    }

    public RentalDTO selectById(int id) {
        RentalDTO rentalDTO = null;
        String query = "select * from `rental` where `rental_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rentalDTO = new RentalDTO();
                rentalDTO.setRentalId(resultSet.getInt("rental_id"));
                rentalDTO.setRentalDate(resultSet.getTimestamp("rental_date"));
                rentalDTO.setInventoryId(resultSet.getInt("inventory_id"));
                rentalDTO.setCustomerId(resultSet.getInt("customer_id"));
                rentalDTO.setReturnDate(resultSet.getTimestamp("return_date"));
                rentalDTO.setStaffId(resultSet.getInt("staff_id"));
            }
        } catch (SQLException e) {
            System.out.println("[rental] selectById error");
            e.printStackTrace();
        }
        return rentalDTO;
    }

    public ArrayList<RentalDTO> selectByReturnDateIsNull() {
        ArrayList<RentalDTO> list = new ArrayList<>();
        String query = "select * from `rental` where `return_date` is null";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RentalDTO rentalDTO = new RentalDTO();
                rentalDTO.setRentalId(resultSet.getInt("rental_id"));
                rentalDTO.setRentalDate(resultSet.getTimestamp("rental_date"));
                rentalDTO.setInventoryId(resultSet.getInt("inventory_id"));
                rentalDTO.setCustomerId(resultSet.getInt("customer_id"));
                rentalDTO.setReturnDate(resultSet.getTimestamp("return_date"));
                rentalDTO.setStaffId(resultSet.getInt("staff_id"));

                list.add(rentalDTO);
            }
        } catch (SQLException e) {
            System.out.println("[rental] selectByReturnDateIsNull error");
            e.printStackTrace();
        }
        return list;
    }

    public void insert(RentalDTO rentalDTO) {
        String query = "insert into `rental`(`rental_date`, `inventory_id`, `customer_id`, `staff_id`) values(now(), ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, rentalDTO.getInventoryId());
            preparedStatement.setInt(2, rentalDTO.getCustomerId());
            preparedStatement.setInt(3, rentalDTO.getStaffId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[rental] insert error");
            e.printStackTrace();
        }
    }

    public void update(RentalDTO rentalDTO) {
        String query = "update `rental` set `return_date` = now() where `rental_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, rentalDTO.getRentalId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[rental] update error");
            e.printStackTrace();
        }
    }
}
