package pl.holidayhouse.customer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer){
        if(customer == null){
            System.err.println("Customer is null");
            return;
        }
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return customerRepository.findAll();
        } else {
            return customerRepository.search(filterText);
        }
    }

    public long count() {
        return customerRepository.count();
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }
}