package pl.holidayhouse.reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.holidayhouse.employee.Employee;
import pl.holidayhouse.house.House;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT * FROM reservation u " +
            "WHERE LOWER(u.reservation_id) LIKE %:searchTerm% " +
            "OR LOWER(u.comment) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Reservation> search(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM house h WHERE h.status = 'aktywny' AND h.house_id NOT IN (" +
            "SELECT DISTINCT u.house_id FROM reservation u " +
            "WHERE (:check_in < u.check_out) AND (:check_out > u.check_in))",
            nativeQuery = true)
    List<House> checkAvailability(@Param("check_in") LocalDate check_in, @Param("check_out") LocalDate check_out);
}