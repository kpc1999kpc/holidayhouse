package holidayhouse.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Reservation not found with id " + id));
    }

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = getById(id);
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
