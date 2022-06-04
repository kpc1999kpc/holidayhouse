package pl.holidayhouse.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    @Query(value = "SELECT * FROM house u " +
            "WHERE LOWER(u.house_id) LIKE %:searchTerm% " +
            "OR LOWER(u.status) LIKE %:searchTerm% " +
            "OR LOWER(u.comment) LIKE %:searchTerm%",
            nativeQuery = true)
    List<House> search(@Param("searchTerm") String searchTerm);
}
