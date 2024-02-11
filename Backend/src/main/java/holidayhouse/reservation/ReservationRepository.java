package holidayhouse.reservation;

import holidayhouse.secutiy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);

    Optional<Reservation> findByIdAndUser(Long id, User user);
}
