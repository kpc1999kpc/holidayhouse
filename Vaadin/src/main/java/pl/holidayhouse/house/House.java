package pl.holidayhouse.house;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="house")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long house_id;
    @NonNull
    private String status;
    @NonNull
    private String comment;
}