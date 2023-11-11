    package holidayhouse.reservation;

    import holidayhouse.customer.Customer;
    import holidayhouse.house.House;
    import com.fasterxml.jackson.annotation.JsonIdentityInfo;
    import com.fasterxml.jackson.annotation.ObjectIdGenerators;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.NonNull;

    import java.time.LocalDate;

    @Entity
    @Table(name="reservation")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public class Reservation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Integer guests_number;
        private LocalDate check_in;
        private LocalDate check_out;
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