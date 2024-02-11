package holidayhouse.house;

import com.fasterxml.jackson.annotation.JsonIgnore;
import holidayhouse.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import holidayhouse.secutiy.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="house")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;
    private String comment;
    private Integer numberOfBeds;

    @OneToMany(mappedBy = "house")
    private Set<Reservation> reservations;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
