package holidayhouse.payment;


import holidayhouse.house.HouseRepository;
import holidayhouse.reservation.Reservation;
import holidayhouse.reservation.ReservationRepository;
import holidayhouse.reservation.ReservationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final HouseRepository houseRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository, ReservationService reservationService, HouseRepository houseRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.houseRepository = houseRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Payment not found with id " + id));
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    public Payment addPayment(PaymentDTO paymentDTO) {
        Long reservationId = extractReservationId(paymentDTO.getReservationDetails());
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + reservationId));

        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setReservation(reservation);

        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, PaymentDTO paymentDetails) {
        Long reservationId = extractReservationId(paymentDetails.getReservationDetails());
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + reservationId));

        Payment payment = getById(id);
        payment.setAmount(paymentDetails.getAmount());
        payment.setReservation(reservation);

        return paymentRepository.save(payment);
    }

    private Long extractReservationId(String reservationDetails) {
        List<Map<String, String>> allDetails = reservationService.getAllFormattedReservationDetails();

        for (Map<String, String> detail : allDetails) {
            if (detail.get("name").equals(reservationDetails)) {
                return Long.parseLong(detail.get("id"));
            }
        }

        throw new IllegalArgumentException("No matching reservation found for given details");
    }

    public class AnnualFinancialSummary {
        private final BigDecimal totalIncome;
        private final BigDecimal incomeTax;

        public AnnualFinancialSummary(BigDecimal totalIncome, BigDecimal incomeTax) {
            this.totalIncome = totalIncome;
            this.incomeTax = incomeTax;
        }

        // Getters
        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public BigDecimal getIncomeTax() {
            return incomeTax;
        }
    }

    public Map<String, Object> getAnnualPaymentSummaryForYear(int year) {
        List<Payment> payments = paymentRepository.findAll();
        BigDecimal annualIncome = BigDecimal.ZERO;
        BigDecimal totalClimateFee = BigDecimal.ZERO;
        int totalGuests = 0;
        int totalReservations = 0;
        Map<Integer, BigDecimal> monthlyEarnings = IntStream.rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toMap(month -> month, month -> BigDecimal.ZERO.setScale(0, RoundingMode.HALF_EVEN)));

        for (Payment payment : payments) {
            if (payment.getReservation().getCheck_in().getYear() == year) {
                PaymentDTO paymentDTO = new PaymentDTO(payment);
                int month = payment.getReservation().getCheck_in().getMonthValue();
                annualIncome = annualIncome.add(paymentDTO.getAmount()).setScale(0, RoundingMode.HALF_EVEN);
                totalClimateFee = totalClimateFee.add(paymentDTO.getClimateFee() != null ? paymentDTO.getClimateFee() : BigDecimal.ZERO).setScale(0, RoundingMode.HALF_EVEN);
                totalGuests += payment.getReservation().getGuests_number();
                totalReservations++;

                monthlyEarnings.put(month, monthlyEarnings.get(month).add(paymentDTO.getAmount()).setScale(0, RoundingMode.HALF_EVEN));
            }
        }

        BigDecimal incomeTax = annualIncome.multiply(BigDecimal.valueOf(0.08)).setScale(0, RoundingMode.HALF_EVEN); // 8% podatku dochodowego
        BigDecimal vatTax = annualIncome.multiply(BigDecimal.valueOf(0.23)).setScale(0, RoundingMode.HALF_EVEN); // 23% VAT
        BigDecimal netIncome = annualIncome.subtract(incomeTax).subtract(vatTax).setScale(0, RoundingMode.HALF_EVEN); // Dochód
        String totalHouses = String.valueOf(houseRepository.count());

        NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN); // Używa kropki jako separatora tysięcy

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", formatter.format(annualIncome));
        summary.put("incomeTax", formatter.format(incomeTax));
        summary.put("vatTax", formatter.format(vatTax));
        summary.put("totalClimateFee", formatter.format(totalClimateFee));
        summary.put("totalGuests", formatter.format(totalGuests));
        summary.put("totalReservations", formatter.format(totalReservations));
        summary.put("netIncome", formatter.format(netIncome));
        summary.put("monthlyEarnings", monthlyEarnings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> formatter.format(e.getValue())
                )));
        summary.put("totalHouses", totalHouses);

        return summary;
    }

    public Map<LocalDate, BigDecimal> calculateDailyAveragePrice(int year) {
        List<Payment> payments = paymentRepository.findAll();
        Map<LocalDate, List<BigDecimal>> dailyTotals = new HashMap<>();

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        for (Payment payment : payments) {
            Reservation reservation = payment.getReservation();
            LocalDate checkIn = reservation.getCheck_in();
            LocalDate checkOut = reservation.getCheck_out();

            if (checkOut.getYear() < year || checkIn.getYear() > year) {
                continue; // Pomiń płatności poza podanym rokiem
            }

            // Dostosuj daty do zakresu danego roku
            LocalDate adjustedCheckIn = (checkIn.isBefore(startOfYear)) ? startOfYear : checkIn;
            LocalDate adjustedCheckOut = (checkOut.isAfter(endOfYear)) ? endOfYear : checkOut;

            long days = ChronoUnit.DAYS.between(adjustedCheckIn, adjustedCheckOut);

            if (days <= 0) {
                continue; // Pomiń, jeśli nie ma dni do obliczeń
            }

            BigDecimal dailyRate = payment.getAmount().divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);

            for (LocalDate date = adjustedCheckIn; !date.isAfter(adjustedCheckOut); date = date.plusDays(1)) {
                dailyTotals.computeIfAbsent(date, k -> new ArrayList<>()).add(dailyRate);
            }
        }

        Map<LocalDate, BigDecimal> dailyAverage = new TreeMap<>(); // Używamy TreeMap
        for (Map.Entry<LocalDate, List<BigDecimal>> entry : dailyTotals.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                BigDecimal sum = entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal average = sum.divide(BigDecimal.valueOf(entry.getValue().size()), 2, RoundingMode.HALF_UP);
                dailyAverage.put(entry.getKey(), average);
            }
        }

        return dailyAverage;
    }
}
