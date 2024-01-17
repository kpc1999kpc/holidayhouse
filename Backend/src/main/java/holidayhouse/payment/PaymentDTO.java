package holidayhouse.payment;

import holidayhouse.reservation.Reservation;
import holidayhouse.reservation.ReservationDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class PaymentDTO {

    private Long id;
    private BigDecimal amount;
    private String reservationDetails; // Dodatkowe szczegóły rezerwacji
    private Reservation reservation; // Bezpośrednie odniesienie do obiektu Reservation
    private BigDecimal climateFee; // Opłata klimatyczna
    private BigDecimal dailyRate; // Cena za dobę

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.amount = payment.getAmount();
        this.reservation = payment.getReservation();

        // Konwersja obiektu Reservation na ReservationDTO
        ReservationDTO reservationDTO = new ReservationDTO(reservation);
        this.reservationDetails = reservationDTO.getFormattedReservationDetails();

        // Obliczenie opłaty klimatycznej
        this.climateFee = calculateClimateFee(reservationDTO);

        // Obliczenie ceny za dobę
        this.dailyRate = calculateDailyRate(reservationDTO);
    }

    // Konstruktor bezparametrowy
    public PaymentDTO() {
    }

    private BigDecimal calculateClimateFee(ReservationDTO reservationDTO) {
        // Ustalenie stawki opłaty klimatycznej na podstawie roku
        BigDecimal rate = (reservationDTO.getCheck_in() != null && reservationDTO.getCheck_in().getYear() >= 2023)
                ? new BigDecimal("2.80")
                : new BigDecimal("2.50");

        // Obliczenie całkowitej opłaty klimatycznej
        // Liczba nocy pomnożona przez liczbę osób i stawkę za dany rok
        return rate.multiply(BigDecimal.valueOf(reservationDTO.getNights()))
                .multiply(BigDecimal.valueOf(reservationDTO.getGuests_number()));
    }

    private BigDecimal calculateDailyRate(ReservationDTO reservationDTO) {
        // Sprawdzamy, czy liczba nocy nie jest równa zero, aby uniknąć dzielenia przez zero.
        if (reservationDTO.getNights() == 0) {
            // Można zwrócić BigDecimal.ZERO lub inną wartość domyślną
            return BigDecimal.ZERO;
        }

        // Ustalamy cenę za dobę dzieląc całkowitą kwotę przez liczbę nocy
        // i zaokrąglając wynik do pełnej liczby.
        return this.amount.divide(BigDecimal.valueOf(reservationDTO.getNights()), 0, RoundingMode.HALF_UP);
    }

}
