package holidayhouse.customer;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Customer not found with id " + id));
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getById(id);
        customer.setName(customerDetails.getName());
        customer.setSurname(customerDetails.getSurname());
        customer.setPhone_number(customerDetails.getPhone_number());
        customer.setComment(customerDetails.getComment());
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
