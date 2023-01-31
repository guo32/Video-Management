package controller;

import model.StaffDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffController {
    private final Connection CONNECTION;

    public StaffController(Connection connection) {
        CONNECTION = connection;
    }

    /* 직원 번호로 직원 검색 */
    public StaffDTO selectById(int id) {
        StaffDTO staffDTO = null;
        String query = "select * from `staff` where `staff_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                staffDTO = new StaffDTO();
                staffDTO.setStaffId(resultSet.getInt("staff_id"));
                staffDTO.setFirstName(resultSet.getString("first_name"));
                staffDTO.setLastName(resultSet.getString("last_name"));
                staffDTO.setAddressId(resultSet.getInt("address_id"));
                staffDTO.setEmail(resultSet.getString("email"));
                staffDTO.setStoreId(resultSet.getInt("store_id"));
                staffDTO.setActive(resultSet.getInt("active"));
                staffDTO.setUsername(resultSet.getString("username"));
                staffDTO.setPassword(resultSet.getString("password"));
                staffDTO.setLastUpdate(resultSet.getTimestamp("last_update"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[staff] selectById error");
            e.printStackTrace();
        }
        return staffDTO;
    }

    public boolean validateUsername(String username) {
        String query = "select `username` from `staff` where `username` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[staff] validateUsername error");
            e.printStackTrace();
        }
        return false;
    }

    public StaffDTO auth(String username, String password) {
        StaffDTO staffDTO = null;
        String query = "select * from `staff` where `username` = ? and `password` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                staffDTO = new StaffDTO();
                staffDTO.setStaffId(resultSet.getInt("staff_id"));
                staffDTO.setFirstName(resultSet.getString("first_name"));
                staffDTO.setLastName(resultSet.getString("last_name"));
                staffDTO.setStoreId(resultSet.getInt("store_id"));
                staffDTO.setActive(resultSet.getInt("active"));
                staffDTO.setUsername(resultSet.getString("username"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[staff] auth error");
            e.printStackTrace();
        }
        return staffDTO;
    }

    public ArrayList<StaffDTO> selectAll() {
        ArrayList<StaffDTO> list = new ArrayList<>();
        String query = "select * from `staff`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StaffDTO staffDTO = new StaffDTO();
                staffDTO.setStaffId(resultSet.getInt("staff_id"));
                staffDTO.setFirstName(resultSet.getString("first_name"));
                staffDTO.setLastName(resultSet.getString("last_name"));
                staffDTO.setAddressId(resultSet.getInt("address_id"));
                staffDTO.setEmail(resultSet.getString("email"));
                staffDTO.setStoreId(resultSet.getInt("store_id"));
                staffDTO.setActive(resultSet.getInt("active"));
                staffDTO.setUsername(resultSet.getString("username"));
                staffDTO.setLastUpdate(resultSet.getTimestamp("last_update"));

                list.add(staffDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[staff] selectAll error");
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(StaffDTO staffDTO) {
        String query = "insert into `staff`(`first_name`, `last_name`, `address_id`, `email`, `store_id`, `active`, `username`, `password`, `last_update`) values(?, ?, ?, ?, ?, 1, ?, ?, now())";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);

            preparedStatement.setString(1, staffDTO.getFirstName());
            preparedStatement.setString(2, staffDTO.getLastName());
            preparedStatement.setInt(3, staffDTO.getAddressId());
            preparedStatement.setString(4, staffDTO.getEmail());
            preparedStatement.setInt(5, staffDTO.getStoreId());
            preparedStatement.setString(6, staffDTO.getUsername());
            preparedStatement.setString(7, staffDTO.getPassword());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            // System.out.println("[staff] insert error");
            return false;
        }
        return true;
    }

    public void update(StaffDTO staffDTO) {
        String query = "update `staff` set `store_id` = ?, `active` = ?, `password` = ? where `staff_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);

            preparedStatement.setInt(1, staffDTO.getStoreId());
            preparedStatement.setInt(2, staffDTO.getActive());
            preparedStatement.setString(3, staffDTO.getPassword());
            preparedStatement.setInt(4, staffDTO.getStaffId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[staff] update error");
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "delete from `staff` where `staff_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[staff] delete error");
            e.printStackTrace();
        }
    }
}
