package pl.holidayhouse.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT * FROM employee u " +
            "WHERE LOWER(u.name) LIKE %:searchTerm% " +
            "OR LOWER(u.surname) LIKE %:searchTerm%",
            nativeQuery = true)
    List<Employee> search(@Param("searchTerm") String searchTerm);
}
