package holidayhouse.reservation;

import holidayhouse.customer.Customer;
import holidayhouse.customer.CustomerRepository;
import holidayhouse.house.House;
import holidayhouse.house.HouseRepository;
import holidayhouse.secutiy.demo.AbstractService;
import holidayhouse.secutiy.user.User;
import jakarta.persistence.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService extends AbstractService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private HouseRepository houseRepository;

    public List<Reservation> getAllReservations() {
        User loggedInUser = getLoggedInUser();
        return reservationRepository.findByUser(loggedInUser);
    }

    public Reservation getById(Long id) {
        User loggedInUser = getLoggedInUser();
        return reservationRepository.findByIdAndUser(id, loggedInUser).orElseThrow(() -> new NoSuchElementException("Reservation not found with id " + id + "for the logged-in user"));
    }

    public Reservation addReservation(ReservationDTO reservationDetails) {
        User loggedInUser = getLoggedInUser();
        Reservation newReservation = new Reservation();
        newReservation.setUser(loggedInUser);
        updateReservationDetails(newReservation, reservationDetails);
        return reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long id, ReservationDTO reservationDetails) {
        Reservation existingReservation = getById(id);
        updateReservationDetails(existingReservation, reservationDetails);
        return reservationRepository.save(existingReservation);
    }

    public void delete(Long id) {
        Reservation reservation = getById(id);
        reservationRepository.delete(reservation);
    }

    public List<Map<String, String>> getAllFormattedReservationDetails() {
        User loggedInUser = getLoggedInUser();
        return reservationRepository.findByUser(loggedInUser).stream()
                .map(reservation -> {
                    Map<String, String> detailsMap = new HashMap<>();
                    ReservationDTO reservationDTO = new ReservationDTO(reservation);
                    detailsMap.put("id", reservationDTO.getId().toString());
                    detailsMap.put("name", reservationDTO.getFormattedReservationDetails());

                    return detailsMap;
                })
                .collect(Collectors.toList());
    }

    private Reservation updateReservationDetails(Reservation reservation, ReservationDTO reservationDetails) {
        User loggedInUser = getLoggedInUser();
        reservation.setGuests_number(reservationDetails.getGuests_number());
        reservation.setCheck_in(reservationDetails.getCheck_in());
        reservation.setCheck_out(reservationDetails.getCheck_out());
        reservation.setComment(reservationDetails.getComment());

        if (reservationDetails.getCustomerFullName() != null && !reservationDetails.getCustomerFullName().isBlank()) {
            String[] fullName = reservationDetails.getCustomerFullName().split(" ", 2);
            String surname = fullName[0];
            String name = fullName.length > 1 ? fullName[1] : "";
            Customer customer = customerRepository.findByNameAndSurnameAndUser(name, surname, loggedInUser)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with name " + name + " and surname " + surname + "for the logged-in user"));
            reservation.setCustomer(customer);
        }

        if (reservationDetails.getHouseName() != null && !reservationDetails.getHouseName().isBlank()) {
            House house = houseRepository.findByNameAndUser(reservationDetails.getHouseName(), loggedInUser)
                    .orElseThrow(() -> new EntityNotFoundException("House not found with name " + reservationDetails.getHouseName()));
            reservation.setHouse(house);
        }

        return reservation;
    }

    public Map<Long, List<Long>> findAllCollisions() {
        List<Reservation> allReservations = getAllReservations();
        Map<Long, List<Long>> collisionsMap = new HashMap<>();

        for (int i = 0; i < allReservations.size(); i++) {
            for (int j = i + 1; j < allReservations.size(); j++) {
                Reservation res1 = allReservations.get(i);
                Reservation res2 = allReservations.get(j);

                if (isOverlapping(res1, res2)) {
                    collisionsMap.computeIfAbsent(res1.getId(), k -> new ArrayList<>()).add(res2.getId());
                    collisionsMap.computeIfAbsent(res2.getId(), k -> new ArrayList<>()).add(res1.getId());
                }
            }
        }
        return collisionsMap;
    }

    private boolean isOverlapping(Reservation res1, Reservation res2) {
        return res1.getHouse().getId().equals(res2.getHouse().getId()) &&
                res1.getCheck_in().isBefore(res2.getCheck_out()) &&
                res1.getCheck_out().isAfter(res2.getCheck_in());
    }

}
