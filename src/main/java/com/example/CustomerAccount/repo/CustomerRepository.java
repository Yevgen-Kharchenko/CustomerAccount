package com.example.CustomerAccount.repo;

import com.example.CustomerAccount.model.Customer;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustomerRepository {
    public static final String KEY = "ACCOUNT";
    private RedisTemplate<String, Customer> redisTemplate;
    private HashOperations hashOperations;

    public CustomerRepository(RedisTemplate<String, Customer> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public Map<Integer,Customer> getAllCustomers(){
        return hashOperations.entries(KEY);
    }

    public Customer getCustomer(int itemId){
        return (Customer) hashOperations.get(KEY,itemId);
    }

    public void addCustomer(Customer customer){
        hashOperations.put(KEY,customer.getId(),customer);
    }

    public void deleteCustomer(Long id){
        hashOperations.delete(KEY,id);
    }

    public void updateCustomer(Customer customer){
        addCustomer(customer);
    }
}
