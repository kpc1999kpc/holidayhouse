package holidayhouse.reservation;

import holidayhouse.customer.Customer;
import holidayhouse.house.House;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTO {

    private Long id;
    private Integer guestsNumber;
    private DateRange dateRange; // Używając klasy DateRange
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
        this.dateRange = new DateRange(reservation.getCheck_in(), reservation.getCheck_out());
        this.comment = reservation.getComment();

        Customer customer = reservation.getCustomer();
        this.customerFullName = (customer != null && customer.getSurname() != null ? customer.getSurname() : "") +
                (customer != null && customer.getName() != null ? " " + customer.getName() : "");

        this.customerId = (customer != null ? customer.getId() : null);

        House house = reservation.getHouse();
        this.houseName = (house != null && house.getName() != null) ? house.getName() : "Nieznany";
        this.houseId = (house != null ? house.getId() : null);
    }

    @Data
    public static class DateRange {
        private LocalDate checkIn;
        private LocalDate checkOut;

        public DateRange(LocalDate checkIn, LocalDate checkOut) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
        }
    }
}
