package controller;

import model.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private final Connection CONNECTION;

    public CustomerController(Connection connection) {
        CONNECTION = connection;
    }

    public CustomerDTO selectById(int id) {
        CustomerDTO customerDTO = null;
        String query = "select * from `customer` where `customer_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customerDTO = new CustomerDTO();
                customerDTO.setCustomerId(resultSet.getInt("customer_id"));
                customerDTO.setStoreId(resultSet.getInt("store_id"));
                customerDTO.setFirstName(resultSet.getString("first_name"));
                customerDTO.setLastName(resultSet.getString("last_name"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setAddressId(resultSet.getInt("address_id"));
                customerDTO.setActive(resultSet.getInt("active"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] selectById error");
            e.printStackTrace();
        }
        return customerDTO;
    }

    public ArrayList<CustomerDTO> selectAll() {
        ArrayList<CustomerDTO> list = new ArrayList<>();
        String query = "select * from `customer`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerId(resultSet.getInt("customer_id"));
                customerDTO.setStoreId(resultSet.getInt("store_id"));
                customerDTO.setFirstName(resultSet.getString("first_name"));
                customerDTO.setLastName(resultSet.getString("last_name"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setAddressId(resultSet.getInt("address_id"));
                customerDTO.setActive(resultSet.getInt("active"));

                list.add(customerDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] selectAll error");
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<CustomerDTO> selectByFirstName(String firstName) {
        ArrayList<CustomerDTO> list = new ArrayList<>();
        String query = "select * from `customer` where `first_name` like ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, "%" + firstName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerId(resultSet.getInt("customer_id"));
                customerDTO.setStoreId(resultSet.getInt("store_id"));
                customerDTO.setFirstName(resultSet.getString("first_name"));
                customerDTO.setLastName(resultSet.getString("last_name"));
                customerDTO.setEmail(resultSet.getString("email"));
                customerDTO.setAddressId(resultSet.getInt("address_id"));
                customerDTO.setActive(resultSet.getInt("active"));

                list.add(customerDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] selectByFirstName error");
            e.printStackTrace();
        }
        return list;
    }

    public void insert(CustomerDTO customerDTO) {
        String query = "insert into `customer`(`store_id`, `first_name`, `last_name`, `email`, `address_id`, `active`, `create_date`) values(?, ?, ?, ?, ?, 1, now())";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, customerDTO.getStoreId());
            preparedStatement.setString(2, customerDTO.getFirstName());
            preparedStatement.setString(3, customerDTO.getLastName());
            preparedStatement.setString(4, customerDTO.getEmail());
            preparedStatement.setInt(5, customerDTO.getAddressId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] insert error");
            e.printStackTrace();
        }
    }

    public void update(CustomerDTO customerDTO) {
        String query = "update `customer` set `address_id` = ?, `active` = ? where `customer_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, customerDTO.getAddressId());
            preparedStatement.setInt(2, customerDTO.getActive());
            preparedStatement.setInt(3, customerDTO.getCustomerId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] update error");
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "delete from `customer` where `customer_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[customer] delete error");
            e.printStackTrace();
        }
    }
}
