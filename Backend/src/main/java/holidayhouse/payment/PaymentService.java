package holidayhouse.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Payment not found with id " + id));
    }

    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = getById(id);
        return paymentRepository.save(payment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
