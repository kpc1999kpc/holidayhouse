package holidayhouse.reservation;

import holidayhouse.customer.Customer;
import holidayhouse.house.House;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
public class ReservationDTO {

    private Long id;
    private Integer guests_number;
    private LocalDate check_in;
    private LocalDate check_out;
    private String comment;
    private Long customerId;
    private String customerFullName; // Zakładam, że w klasie Customer są pola firstName i lastName
    private Long houseId;
    private String houseName; // Zakładam, że w klasie House jest pole name
    private Integer nights;

    // Konstruktor bezparametrowy
    public ReservationDTO() {
    }

    // Konstruktor z wszystkimi polami
    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.guests_number = reservation.getGuests_number();
        this.check_in = reservation.getCheck_in();
        this.check_out = reservation.getCheck_out();
        this.comment = reservation.getComment();

        if(this.check_in != null && this.check_out != null) {
            this.nights = (int) ChronoUnit.DAYS.between(this.check_in, this.check_out);
        }

        // Łączenie nazwiska i imienia klienta w jednym polu z uwzględnieniem wartości null
        Customer customer = reservation.getCustomer();
        this.customerFullName = (customer != null && customer.getSurname() != null ? customer.getSurname() : "") +
                (customer != null && customer.getName() != null ? " " + customer.getName() : "");

        // Ustawienie customerId
        this.customerId = (customer != null ? customer.getId() : null);

        // Bezpieczne pobieranie nazwy domu i jego ID z obiektu house
        House house = reservation.getHouse();
        this.houseName = (house != null && house.getName() != null) ? house.getName() : "Nieznany";
        // Ustawienie houseId
        this.houseId = (house != null ? house.getId() : null);
    }

}