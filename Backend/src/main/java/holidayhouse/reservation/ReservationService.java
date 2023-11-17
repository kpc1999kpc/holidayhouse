package holidayhouse.reservation;

import holidayhouse.customer.Customer;
import holidayhouse.customer.CustomerRepository;
import holidayhouse.house.House;
import holidayhouse.house.HouseRepository;
import jakarta.persistence.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final HouseRepository houseRepository;

    public ReservationService(ReservationRepository reservationRepository, CustomerRepository customerRepository, HouseRepository houseRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.houseRepository = houseRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Reservation not found with id " + id));
    }

    public Reservation addReservation(ReservationDTO reservationDetails) {
        Reservation newReservation = new Reservation();
        updateReservationDetails(newReservation, reservationDetails);
        return reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long id, ReservationDTO reservationDetails) {
        Reservation existingReservation = getById(id);
        updateReservationDetails(existingReservation, reservationDetails);
        return reservationRepository.save(existingReservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    private Reservation updateReservationDetails(Reservation reservation, ReservationDTO reservationDetails) {
        reservation.setGuests_number(reservationDetails.getGuests_number());
        reservation.setCheck_in(reservationDetails.getCheck_in());
        reservation.setCheck_out(reservationDetails.getCheck_out());
        reservation.setComment(reservationDetails.getComment());

        if (reservationDetails.getCustomerFullName() != null && !reservationDetails.getCustomerFullName().isBlank()) {
            String[] fullName = reservationDetails.getCustomerFullName().split(" ", 2);
            String surname = fullName[0];
            String name = fullName.length > 1 ? fullName[1] : "";
            Customer customer = customerRepository.findByNameAndSurname(name, surname)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with name " + name + " and surname " + surname));
            reservation.setCustomer(customer);
        }

        if (reservationDetails.getHouseName() != null && !reservationDetails.getHouseName().isBlank()) {
            House house = houseRepository.findByName(reservationDetails.getHouseName())
                    .orElseThrow(() -> new EntityNotFoundException("House not found with name " + reservationDetails.getHouseName()));
            reservation.setHouse(house);
        }

        return reservation;
    }

}
