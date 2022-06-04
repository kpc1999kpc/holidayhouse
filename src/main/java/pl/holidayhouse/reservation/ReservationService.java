package pl.holidayhouse.reservation;

import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void save(Reservation reservation){
        if(reservation == null){
            System.err.println("Reservation is null");
            return;
        }
        reservationRepository.save(reservation);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return reservationRepository.findAll();
        } else {
            return reservationRepository.search(filterText);
        }
    }

    public long count() {
        return reservationRepository.count();
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}