package holidayhouse.house;

import holidayhouse.customer.Customer;
import holidayhouse.secutiy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByStatus(String status);
    Optional<House> findByName(String name);
    long countByUser(User user);

    List<House> findByUser(User user);

    Optional<House> findByIdAndUser(Long id, User user);

    Optional<House> findByNameAndUser(String name, User loggedInUser);
}
