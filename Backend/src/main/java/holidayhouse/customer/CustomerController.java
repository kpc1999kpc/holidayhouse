package holidayhouse.customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping({"/{id}"})
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping({"/{id}"})
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return customerService.updateCustomer(id, customerDetails);
    }

    @DeleteMapping({"/{id}"})
    public void deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
    }
}