package holidayhouse.customer;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public List<Map<String, Object>> getAllCustomerFullNames() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    Map<String, Object> customerNameMap = new HashMap<>();
                    String fullName = customer.getSurname() + " " + customer.getName();
                    customerNameMap.put("id", customer.getId());
                    customerNameMap.put("name", fullName.trim()); // Usuń białe znaki na początku i końcu
                    return customerNameMap;
                })
                .collect(Collectors.toList());
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
