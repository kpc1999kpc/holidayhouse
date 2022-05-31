package pl.holidayhouse.employee;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="employee")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employee_id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String id_card_number;
    @NonNull
    private LocalDate employment_date;
    @NonNull
    private String phone_number;
    @NonNull
    private String email;
    @NonNull
    private String address;
    @NonNull
    private String account_number;
    @NonNull
    private String employment_status;
    @NonNull
    private String comment;
}
/*JSON
{
    "name":"name",
    "surname":"surname",
    "id_card_number":"id_card_number",
    "employment_date":"2012-04-23",
    "phone_number":"phone_number",
    "email":"email",
    "address":"address",
    "account_number":"account_number",
    "employment_status":"employment_status",
    "comment":"comment"
}
 */


