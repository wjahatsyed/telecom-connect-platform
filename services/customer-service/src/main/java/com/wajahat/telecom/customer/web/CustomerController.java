package com.wajahat.telecom.customer.web;

import com.wajahat.telecom.customer.dto.CreateCustomerRequest;
import com.wajahat.telecom.customer.dto.CustomerResponse;
import com.wajahat.telecom.customer.service.CustomerService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = CustomerResponse.from(customerService.create(request));
        return ResponseEntity
                .created(URI.create("/customers/" + response.customerId()))
                .body(response);
    }

    @GetMapping("/{customerId}")
    CustomerResponse getById(@PathVariable UUID customerId) {
        return CustomerResponse.from(customerService.getById(customerId));
    }

    @GetMapping
    List<CustomerResponse> getAll() {
        return customerService.getAll().stream()
                .map(CustomerResponse::from)
                .toList();
    }
}
