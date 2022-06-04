package pl.holidayhouse.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.holidayhouse.house.House;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT * FROM payment u " +
            "WHERE LOWER(u.payment_date) LIKE %:searchTerm% " +
            "OR LOWER(u.payment_id) LIKE %:searchTerm% " +
            "OR LOWER(u.amount) LIKE %:searchTerm% " +
            "OR LOWER(u.comment) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Payment> search(@Param("searchTerm") String searchTerm);
}
