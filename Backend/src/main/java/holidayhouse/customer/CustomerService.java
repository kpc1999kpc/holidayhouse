package holidayhouse.customer;

import holidayhouse.secutiy.demo.AbstractService;
import holidayhouse.secutiy.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CustomerService extends AbstractService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        User user = getLoggedInUser();
        return customerRepository.findByUser(user);
    }

    public Customer getById(Long id) {
        User loggedInUser = getLoggedInUser();
        return  customerRepository.findByIdAndUser(id, loggedInUser)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id " + id + " for the logged-in user"));
    }

    public List<Map<String, Object>> getAllCustomerFullNames() {
        User loggedInUser = getLoggedInUser();
        return customerRepository.findByUser(loggedInUser).stream()
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
        User loggedInUser = getLoggedInUser();
        customer.setUser(loggedInUser); // Przypisz zalogowanego użytkownika do klienta
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getById(id); // Pobiera klienta i sprawdza, czy należy do zalogowanego użytkownika

        // Aktualizacja szczegółów klienta
        customer.setName(customerDetails.getName());
        customer.setSurname(customerDetails.getSurname());
        customer.setPhone_number(customerDetails.getPhone_number());
        customer.setComment(customerDetails.getComment());

        // Zapisanie zaktualizowanego klienta
        return customerRepository.save(customer);
    }


    public void delete(Long id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
    }

}
