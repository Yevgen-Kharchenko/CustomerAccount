package com.example.CustomerAccount.repo;

import com.example.CustomerAccount.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, String> {

}