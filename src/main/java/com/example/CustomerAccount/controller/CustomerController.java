package com.example.CustomerAccount.controller;

import com.example.CustomerAccount.model.Customer;
import com.example.CustomerAccount.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
public class CustomerController {
    @Autowired
    CustomerRepository customerRepo;

    @RequestMapping("/getAllCustomers")
    @ResponseBody
    public ResponseEntity<Map<Integer, Customer>> getAllCustomers(){
        Map<Integer,Customer> customers =  customerRepo.getAllCustomers();
        return new ResponseEntity<Map<Integer,Customer>>(customers, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomer(@PathVariable int customerId){
        Customer customer = customerRepo.getCustomer(customerId);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @PostMapping(value = "/addCustomer",consumes = {"application/json"},produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer, UriComponentsBuilder builder){
        customerRepo.addCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/addCustomer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Customer>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/updateCustomer")
    @ResponseBody
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer){
        if(customer != null){
            customerRepo.updateCustomer(customer);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerRepo.deleteCustomer(id);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
