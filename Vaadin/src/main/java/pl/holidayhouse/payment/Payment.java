package pl.holidayhouse.payment;

import lombok.*;
import pl.holidayhouse.reservation.Reservation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payment_id;
    @NonNull
    private LocalDate payment_date;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String comment;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}



