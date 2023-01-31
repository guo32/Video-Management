package controller;

import model.CityCountryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CityCountryController {
    private final Connection CONNECTION;

    public CityCountryController(Connection connection) {
        CONNECTION = connection;
    }

    public CityCountryDTO selectById(int id) {
        CityCountryDTO cityCountryDTO = null;
        String query = "select `city_id`, `city`, `country` from `city` inner join `country` on `city`.`country_id` = `country`.`country_id` where `city_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cityCountryDTO = new CityCountryDTO();
                cityCountryDTO.setCityId(resultSet.getInt("city_id"));
                cityCountryDTO.setCity(resultSet.getString("city"));
                cityCountryDTO.setCountry(resultSet.getString("country"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[city and country] selectById error");
            e.printStackTrace();
        }
        return cityCountryDTO;
    }

    public ArrayList<CityCountryDTO> selectByCountry(String country) {
        ArrayList<CityCountryDTO> list = new ArrayList<>();
        String query = "select `city_id`, `city`, `country` from `city` inner join `country` on `city`.`country_id` = `country`.`country_id` where `country` like ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, "%" + country + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CityCountryDTO cityCountryDTO = new CityCountryDTO();
                cityCountryDTO.setCityId(resultSet.getInt("city_id"));
                cityCountryDTO.setCity(resultSet.getString("city"));
                cityCountryDTO.setCountry(resultSet.getString("country"));

                list.add(cityCountryDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[city and country] selectByCountry error");
            e.printStackTrace();
        }
        return list;
    }
}
