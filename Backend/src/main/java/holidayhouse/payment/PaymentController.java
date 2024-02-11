package holidayhouse.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping({"/{id}"})
    public Payment getPayment(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    @PostMapping
    public Payment addPayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentService.addPayment(paymentDTO);
    }

    @PutMapping({"/{id}"})
    public Payment updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDetails) {
        return paymentService.updatePayment(id, paymentDetails);
    }

    @DeleteMapping({"/{id}"})
    public void deletePayment(@PathVariable Long id) {
        paymentService.delete(id);
    }

    @GetMapping("/annual-summary/{year}")
    public Map<String, Object> getAnnualPaymentSummaryForYear(@PathVariable int year) {
        return paymentService.getAnnualPaymentSummaryForYear(year);
    }

    @GetMapping("/daily-average/{year}")
    public Map<LocalDate, BigDecimal> getDailyAveragePrice(@PathVariable int year) {
        return paymentService.calculateDailyAveragePrice(year);
    }

    @GetMapping("/sum")
    public List<Payment> sum() {
        return paymentService.sum();
    }
}

