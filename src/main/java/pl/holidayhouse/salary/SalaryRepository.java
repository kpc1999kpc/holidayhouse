package pl.holidayhouse.salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.holidayhouse.house.House;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Query(value = "SELECT * FROM salary u " +
            "WHERE LOWER(u.salary_id) LIKE %:searchTerm% " +
            "OR LOWER(u.salary_date) LIKE %:searchTerm% " +
            "OR LOWER(u.comment) LIKE %:searchTerm% " +
            "OR LOWER(u.amount) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Salary> search(@Param("searchTerm") String searchTerm);
}
