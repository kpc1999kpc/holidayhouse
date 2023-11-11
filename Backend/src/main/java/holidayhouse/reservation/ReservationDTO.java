package holidayhouse.reservation;

import holidayhouse.customer.Customer;
import holidayhouse.house.House;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTO {

    private Long id;
    private Integer guestsNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String comment;
    private Long customerId;
    private String customerFullName; // Zakładam, że w klasie Customer są pola firstName i lastName
    private Long houseId;
    private String houseName; // Zakładam, że w klasie House jest pole name

    // Konstruktor bezparametrowy
    public ReservationDTO() {
    }

    // Konstruktor z wszystkimi polami
    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.guestsNumber = reservation.getGuests_number();
        this.checkIn = reservation.getCheck_in();
        this.checkOut = reservation.getCheck_out();
        this.comment = reservation.getComment();

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
