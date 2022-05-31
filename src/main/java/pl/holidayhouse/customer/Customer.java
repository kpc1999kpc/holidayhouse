package pl.holidayhouse.customer;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="customer")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customer_id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String id_card_number;
    @NonNull
    private String phone_number;
    @NonNull
    private String email;
    @NonNull
    private String address;
    @NonNull
    private String nationality;
    @NonNull
    private String comment;
}



