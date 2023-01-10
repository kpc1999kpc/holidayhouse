package pl.holidayhouse.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM customer u " +
            "WHERE LOWER(u.name) LIKE %:searchTerm% " +
            "OR LOWER(u.surname) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Customer> search(@Param("searchTerm") String searchTerm);
}
