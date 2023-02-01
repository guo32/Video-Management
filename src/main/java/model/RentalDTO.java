package model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RentalDTO {
    private int rentalId;
    private Timestamp rentalDate;
    private int inventoryId;
    private int customerId;
    private Timestamp returnDate;
    private int staffId;
}
