package com.wajahat.telecom.customer;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wajahat.telecom.customer.domain.Customer;
import com.wajahat.telecom.customer.repository.CustomerRepository;
import com.wajahat.telecom.customer.service.CustomerService;
import com.wajahat.telecom.customer.web.CustomerController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
@Import({CustomerService.class, CustomerControllerTests.TestConfig.class})
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndReadsCustomer() throws Exception {
        String response = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "ENTERPRISE",
                                  "displayName": "Acme Connectivity",
                                  "email": "ops@acme.test",
                                  "phoneNumber": "+15551234567"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.type").value("ENTERPRISE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String customerId = com.jayway.jsonpath.JsonPath.read(response, "$.customerId");

        mockMvc.perform(get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void rejectsInvalidCustomerRequest() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "CONSUMER",
                                  "displayName": "",
                                  "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        CustomerRepository customerRepository() {
            return new CustomerRepository() {
                private final java.util.Map<UUID, Customer> customers = new LinkedHashMap<>();

                @Override
                public Customer save(Customer customer) {
                    customers.put(customer.customerId(), customer);
                    return customer;
                }

                @Override
                public Optional<Customer> findById(UUID customerId) {
                    return Optional.ofNullable(customers.get(customerId));
                }

                @Override
                public List<Customer> findAll() {
                    return List.copyOf(customers.values());
                }
            };
        }
    }
}
