package com.example.CustomerAccount.controller;

import com.example.CustomerAccount.model.Customer;
import com.example.CustomerAccount.repo.CustomerRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.match.JsonPathRequestMatchers;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Calendar;
import java.util.Optional;

import static java.util.OptionalDouble.empty;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
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
    void getAllCustomers_shouldReturnNoneFound_whenNoDataExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getCustomer() throws Exception {
//        Mockito.when(repository.findAll())
//                .thenReturn(asList(
//                        Customer.builder().id(1L).firstName("first").build(),
//                        Customer.builder().id(2L).firstName("first").build()
//                ));
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/addAllCustomers"))
//                .andExpect(status().isOk())
//                .andReturn();
//        Mockito.verify(repository).findAll();
//
//        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
//        List customerList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
//
//        Assert.assertThat(customerList.size(), IsEqual.equalTo(2));
    }

    @Test
    void addCustomer() throws Exception {
        Mockito.when(repository.save(any(Customer.class))).thenReturn(Customer.builder().id("1").build());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/addCustomer")
                .param("firstName", "first")
                .param("lastName", "last"))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(repository).save(any(Customer.class));
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().equals("1"));
    }

    @Test
    void updateCustomer() throws Exception {

        Customer savedCustomer = Customer.builder().firstName("aaa").lastName("sss").build();
        Customer changedCustomer = Customer.builder().firstName("bbb").lastName("zzz").build();
        when(repository.findById(anyString())).thenReturn(Optional.of(savedCustomer));
//        when(repository.save(any(Customer.class))).thenReturn(changedCustomer);

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
                .andExpect(jsonPath("$.firstName").value("bbb"))
                .andExpect(status().isOk());

    }

    @Test
    void deleteCustomer() {
    }
}