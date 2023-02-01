package model;

import lombok.Data;

import java.time.Year;

@Data
public class FilmDTO {
    private int filmId;
    private String title;
    private String description;
    private Year releaseYear;
    private int languageId;
    private int rentalDuration;
    private Double rentalRate;
    private int length;
    private Double replacementCost;
    private String rating;
    private String specialFeatures;
}
