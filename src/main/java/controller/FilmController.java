package controller;

import model.FilmDTO;
import model.InventoryFilmDTO;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;

public class FilmController {
    private final Connection CONNECTION;

    public FilmController(Connection connection) {
        CONNECTION = connection;
    }

    public void insert(FilmDTO filmDTO) {
        String query = "insert into `film`(`title`, `description`, `release_year`, `language_id`, `rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`, `special_features`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, '" + filmDTO.getSpecialFeatures() + "')";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, filmDTO.getTitle());
            preparedStatement.setString(2, filmDTO.getDescription());
            preparedStatement.setTime(3, Time.valueOf(filmDTO.getReleaseYear().toString()));
            preparedStatement.setInt(4, filmDTO.getLanguageId());
            preparedStatement.setInt(5, filmDTO.getRentalDuration());
            preparedStatement.setDouble(6, filmDTO.getRentalRate());
            preparedStatement.setInt(7, filmDTO.getLength());
            preparedStatement.setDouble(8, filmDTO.getReplacementCost());
            preparedStatement.setString(9, filmDTO.getRating());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] insert error");
            e.printStackTrace();
        }
    }

    public FilmDTO selectById(int id) {
        FilmDTO filmDTO = null;
        String query = "select * from `film` where `film_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                filmDTO = new FilmDTO();
                filmDTO.setFilmId(resultSet.getInt("film_id"));
                filmDTO.setTitle(resultSet.getString("title"));
                filmDTO.setDescription(resultSet.getString("description"));
                filmDTO.setReleaseYear(Year.of(resultSet.getInt("release_year")));
                filmDTO.setLanguageId(resultSet.getInt("language_id"));
                filmDTO.setRentalDuration(resultSet.getInt("rental_duration"));
                filmDTO.setRentalRate(resultSet.getDouble("rental_rate"));
                filmDTO.setLength(resultSet.getInt("length"));
                filmDTO.setReplacementCost(resultSet.getDouble("replacement_cost"));
                filmDTO.setRating(resultSet.getString("rating"));
                filmDTO.setSpecialFeatures(resultSet.getString("special_features"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] selectById error");
            e.printStackTrace();
        }
        return filmDTO;
    }

    public ArrayList<FilmDTO> selectAll() {
        ArrayList<FilmDTO> list = new ArrayList<>();
        String query = "select * from `film`";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FilmDTO filmDTO = new FilmDTO();
                filmDTO.setFilmId(resultSet.getInt("film_id"));
                filmDTO.setTitle(resultSet.getString("title"));
                filmDTO.setReleaseYear(Year.of(resultSet.getInt("release_year")));

                list.add(filmDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] selectAll error");
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<FilmDTO> selectByTitle(String title) {
        ArrayList<FilmDTO> list = new ArrayList<>();
        String query = "select * from `film` where `title` like ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, "%" + title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FilmDTO filmDTO = new FilmDTO();
                filmDTO.setFilmId(resultSet.getInt("film_id"));
                filmDTO.setTitle(resultSet.getString("title"));
                filmDTO.setReleaseYear(Year.of(resultSet.getInt("release_year")));

                list.add(filmDTO);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] selectByTitle error");
            e.printStackTrace();
        }

        return list;
    }
}
