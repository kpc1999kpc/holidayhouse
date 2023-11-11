package pl.holidayhouse.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void save(Payment payment){
        if(payment == null){
            System.err.println("Payment is null");
            return;
        }
        paymentRepository.save(payment);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return paymentRepository.findAll();
        } else {
            return paymentRepository.search(filterText);
        }
    }

    public BigDecimal total() {
            return paymentRepository.total();
    }

    public long count() {
        return paymentRepository.count();
    }

    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }
}