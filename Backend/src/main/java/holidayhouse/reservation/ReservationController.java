package holidayhouse.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    /*@GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }*/

    @GetMapping({"/{id}"})
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @PostMapping
    public Reservation addReservation(@RequestBody ReservationDTO reservation) {
        return reservationService.addReservation(reservation);
    }

    @PutMapping({"/{id}"})
    public Reservation updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDetails) {
        return reservationService.updateReservation(id, reservationDetails);
    }

    @DeleteMapping({"/{id}"})
    public void deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
    }

    @GetMapping("/details")
    public List<Map<String, String>> getAllReservationDetails() {
        return reservationService.getAllFormattedReservationDetails();
    }

    @GetMapping("/collisions")
    public Map<Long, List<Long>> checkForCollisions() {
        return reservationService.findAllCollisions();
    }
}
