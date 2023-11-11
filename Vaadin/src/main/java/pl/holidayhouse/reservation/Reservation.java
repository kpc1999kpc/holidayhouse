package pl.holidayhouse.reservation;

import lombok.*;
import pl.holidayhouse.customer.Customer;
import pl.holidayhouse.house.House;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="reservation")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservation_id;
    @NonNull
    private int guests_number;
    @NonNull
    private BigDecimal price_per_night;
    @NonNull
    private LocalDate reservation_date;
    @NonNull
    private LocalDate check_in;
    @NonNull
    private LocalDate check_out;
    @NonNull
    private String comment;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;
}



