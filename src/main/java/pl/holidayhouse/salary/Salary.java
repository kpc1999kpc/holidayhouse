package pl.holidayhouse.salary;

import lombok.*;
import pl.holidayhouse.employee.Employee;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="salary")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long salary_id;
    @NonNull
    private LocalDate salary_date;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String comment;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}



