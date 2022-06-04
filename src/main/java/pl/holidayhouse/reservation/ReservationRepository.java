package pl.holidayhouse.reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.holidayhouse.employee.Employee;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT * FROM reservation u " +
            "WHERE LOWER(u.reservation_id) LIKE %:searchTerm% " +
            "OR LOWER(u.comment) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Reservation> search(@Param("searchTerm") String searchTerm);
}
