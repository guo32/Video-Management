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

    public int insert(FilmDTO filmDTO) {
        int id = 0;
        String query = "insert into `film`(`title`, `description`, `release_year`, `language_id`, `rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`, `special_features`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, '" + filmDTO.getSpecialFeatures() + "')";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setString(1, filmDTO.getTitle());
            preparedStatement.setString(2, filmDTO.getDescription());
            preparedStatement.setInt(3, Integer.parseInt(filmDTO.getReleaseYear().toString()));
            preparedStatement.setInt(4, filmDTO.getLanguageId());
            preparedStatement.setInt(5, filmDTO.getRentalDuration());
            preparedStatement.setDouble(6, filmDTO.getRentalRate());
            preparedStatement.setInt(7, filmDTO.getLength());
            preparedStatement.setDouble(8, filmDTO.getReplacementCost());
            preparedStatement.setString(9, filmDTO.getRating());
            preparedStatement.executeUpdate();

            query = "select `film_id` from `film` order by `last_update` desc limit 1";
            preparedStatement = CONNECTION.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                id = resultSet.getInt("film_id");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] insert error");
            e.printStackTrace();
        }
        return id;
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

    public ArrayList<String> selectActorListById(int id) {
        ArrayList<String> list = new ArrayList<>();
        String query = "select `actor`.`first_name`, `actor`.`last_name` from `film_actor` " +
                "inner join `actor` on `film_actor`.`actor_id` = `actor`.`actor_id` " +
                "inner join `film` on `film_actor`.`film_id` = `film`.`film_id` " +
                "where `film`.`film_id` = ?";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String actorName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                list.add(actorName);
            }
        } catch (SQLException e) {
            System.out.println("[film] selectActorListById error");
            e.printStackTrace();
        }
        return list;
    }

    public void insertFilmActor(int filmId, int actorId) {
        String query = "insert into `film_actor`(`actor_id`, `film_id`) values(?, ?)";
        try {
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, actorId);
            preparedStatement.setInt(2, filmId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("[film] insertFilmActor error");
            e.printStackTrace();
        }
    }
}
