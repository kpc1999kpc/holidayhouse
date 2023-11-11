package holidayhouse.customer;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import holidayhouse.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    private String surname;
    private String phone_number;
    private String comment;

    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;
}



