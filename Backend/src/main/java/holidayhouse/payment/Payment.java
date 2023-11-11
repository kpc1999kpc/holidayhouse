package holidayhouse.payment;

import holidayhouse.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate payment_date;
    private BigDecimal amount;
    private String comment;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}



