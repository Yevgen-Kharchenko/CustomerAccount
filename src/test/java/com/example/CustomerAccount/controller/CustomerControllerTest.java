package com.example.CustomerAccount.controller;

import com.example.CustomerAccount.model.Customer;
import com.example.CustomerAccount.repo.CustomerRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class CustomerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepo repository;

    @Test
    void customersList_shouldReturnNoneFound_whenNoDataExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getAllCustomers_shouldReturnListOfCustomers() throws Exception {
        when(repository.findAll())
                .thenReturn(asList(
                        Customer.builder().id("1").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build(),
                        Customer.builder().id("2").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build(),
                        Customer.builder().id("3").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build()
                ));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/customer"))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(repository).findAll();
        List customerList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        Assert.assertThat(customerList.size(), IsEqual.equalTo(3));
    }

    @Test
    void getCustomer_shouldReturnCustomerById() throws Exception {
        Customer customer = Customer.builder().id("1").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build();
        when(repository.findById(anyString())).thenReturn(Optional.of(customer));
        String value = null;
        try {
            value = objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(status().isOk());
    }

    @Test
    void addCustomer_shouldAddCustomerAndReturnId() throws Exception {
        Customer customer = Customer.builder().id("1").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build();
        Mockito.when(repository.save(any(Customer.class))).thenReturn(customer);
        String value = null;
        try {
            value = objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MvcResult mvcResult = (MvcResult) mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(repository).save(any(Customer.class));
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().equals("1"));
    }

    @Test
    void updateCustomer_shouldUpdateCustomerById() throws Exception {
        Customer savedCustomer = Customer.builder().id("1").firstName("Ivan").lastName("Petrov").phone("123456").login("login").password("password").build();
        Customer changedCustomer = Customer.builder().id("1").firstName("Petro").lastName("Ivanov").phone("654321").login("nigol").password("drowssap").build();
        when(repository.findById(anyString())).thenReturn(Optional.of(savedCustomer));
        when(repository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);
        String value = null;
        try {
            value = objectMapper.writeValueAsString(changedCustomer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mockMvc.perform(MockMvcRequestBuilders.put("/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value("Petro"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.phone").value("654321"))
                .andExpect(jsonPath("$.login").value("nigol"))
                .andExpect(jsonPath("$.password").value("drowssap"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCustomer_shouldDeleteCustomerById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
