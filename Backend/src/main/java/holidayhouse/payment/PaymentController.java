package holidayhouse.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping({"/{id}"})
    public Payment getPayment(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    @PostMapping
    public Payment addPayment(@RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }

    @PutMapping({"/{id}"})
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        return paymentService.updatePayment(id, paymentDetails);
    }

    @DeleteMapping({"/{id}"})
    public void deletePayment(@PathVariable Long id) {
        paymentService.delete(id);
    }
}

