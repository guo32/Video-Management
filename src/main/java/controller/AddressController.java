package controller;

import model.AddressDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressController {
    private final Connection CONNECTION;

    public AddressController(Connection connection) {
        CONNECTION = connection;
    }

    public boolean insert(AddressDTO addressDTO) {
        String query = "insert into `address`(`address`, `district`, `city_id`, `postal_code`, `phone`, `location`) values(?, ?, ?, ?, ?, ST_GeomFromText('POINT(0.0 0.0)'))";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, addressDTO.getAddress());
            preparedStatement.setString(2, addressDTO.getDistrict());
            preparedStatement.setInt(3, addressDTO.getCityId());
            preparedStatement.setString(4, addressDTO.getPostalCode());
            preparedStatement.setString(5, addressDTO.getPhone());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("[address] insert error");
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<AddressDTO> selectByPostalCode(String postalCode) {
        ArrayList<AddressDTO> list = new ArrayList<>();
        String query = "select * from `address` where `postal_code` = ? order by `last_update` desc";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, postalCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressId(resultSet.getInt("address_id"));
                addressDTO.setAddress(resultSet.getString("address"));
                addressDTO.setDistrict(resultSet.getString("district"));
                addressDTO.setPostalCode(resultSet.getString("postal_code"));
                addressDTO.setPhone(resultSet.getString("phone"));

                list.add(addressDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[address] selectByPostalCode error");
            e.printStackTrace();
        }
        return list;
    }

    public AddressDTO selectById(int id) {
        AddressDTO addressDTO = null;
        String query = "select * from `address` where `address_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                addressDTO = new AddressDTO();
                addressDTO.setAddressId(resultSet.getInt("address_id"));
                addressDTO.setAddress(resultSet.getString("address"));
                addressDTO.setDistrict(resultSet.getString("district"));
                addressDTO.setPostalCode(resultSet.getString("postal_code"));
                addressDTO.setPhone(resultSet.getString("phone"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[address] selectById error");
            e.printStackTrace();
        }
        return addressDTO;
    }
}
