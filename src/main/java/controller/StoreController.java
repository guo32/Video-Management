package controller;

import model.StoreDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreController {
    private final Connection CONNECTION;

    public StoreController(Connection connection) {
        CONNECTION = connection;
    }

    public StoreDTO selectById(int id) {
        StoreDTO storeDTO = null;
        String query = "select * from `store` inner join address on `store`.`address_id` = `address`.`address_id` where `store`.`store_id` = ? ";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                storeDTO = new StoreDTO();
                storeDTO.setStoreId(resultSet.getInt("store_id"));
                storeDTO.setManagerStaffId(resultSet.getInt("manager_staff_id"));
                storeDTO.setAddressId(resultSet.getInt("address_id"));
                storeDTO.setAddress(resultSet.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println("[store] selectById error");
            e.printStackTrace();
        }
        return storeDTO;
    }

    public ArrayList<StoreDTO> selectAll() {
        ArrayList<StoreDTO> list = new ArrayList<>();
        String query = "select `store_id`, `manager_staff_id`, `store`.`address_id`, `address`, `district`, `city_id` from `store` inner join `address` on `store`.`address_id` = `address`.`address_id`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StoreDTO storeDTO = new StoreDTO();
                storeDTO.setStoreId(resultSet.getInt("store_id"));
                storeDTO.setManagerStaffId(resultSet.getInt("manager_staff_id"));
                storeDTO.setAddressId(resultSet.getInt("address_id"));
                storeDTO.setAddress(resultSet.getString("address"));

                list.add(storeDTO);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[store] selectAll error");
            e.printStackTrace();
        }
        return list;
    }
}
