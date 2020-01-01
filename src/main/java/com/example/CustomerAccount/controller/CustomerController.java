package com.example.CustomerAccount.controller;

import com.example.CustomerAccount.model.Customer;
import com.example.CustomerAccount.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        Iterable<Customer> customers = customerRepo.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id) {
        if (id != null) {
            Optional<Customer> customer = customerRepo.findById(id);
            return customer.isPresent() ? ResponseEntity.ok(customer.get()) : (ResponseEntity<Customer>) ResponseEntity.ok();
        }
        return (ResponseEntity<Customer>) ResponseEntity.ok();
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        if (customer != null) {
            customerRepo.save(customer);
        }
        assert customer != null;
        return ResponseEntity.ok(customer.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable String id) {
        if (id != null) {
            Optional<Customer> savedCustomer = customerRepo.findById(id);
            if (savedCustomer.isPresent()) {
                Customer updatedCustomer = savedCustomer.get();
                updatedCustomer.setFirstName(customer.getFirstName());
                updatedCustomer.setLastName(customer.getLastName());
                updatedCustomer.setPhone(customer.getPhone());
                updatedCustomer.setLogin(customer.getLogin());
                updatedCustomer.setPassword(customer.getPassword());
                customerRepo.save(updatedCustomer);
                return ResponseEntity.ok(updatedCustomer);
            }
        }
        return (ResponseEntity<Customer>) ResponseEntity.badRequest();
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        customerRepo.deleteById(id);
    }
}
