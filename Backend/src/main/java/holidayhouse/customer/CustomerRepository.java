package holidayhouse.customer;
import holidayhouse.secutiy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNameAndSurname(String name, String surname);

    List<Customer> findByUser(User user);

    Optional<Customer> findByIdAndUser(Long id, User user);

    Optional<Customer> findByNameAndSurnameAndUser(String name, String surname, User loggedInUser);
}
