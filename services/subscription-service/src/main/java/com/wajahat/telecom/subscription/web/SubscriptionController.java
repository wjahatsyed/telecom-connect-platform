package com.wajahat.telecom.subscription.web;

import com.wajahat.telecom.subscription.dto.CreateSubscriptionRequest;
import com.wajahat.telecom.subscription.dto.SubscriptionResponse;
import com.wajahat.telecom.subscription.service.SubscriptionService;
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
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody CreateSubscriptionRequest request) {
        SubscriptionResponse response = SubscriptionResponse.from(subscriptionService.create(request));
        return ResponseEntity.created(URI.create("/subscriptions/" + response.subscriptionId())).body(response);
    }

    @GetMapping("/{subscriptionId}")
    SubscriptionResponse getById(@PathVariable UUID subscriptionId) {
        return SubscriptionResponse.from(subscriptionService.getById(subscriptionId));
    }

    @GetMapping("/device/{deviceId}")
    List<SubscriptionResponse> getByDeviceId(@PathVariable UUID deviceId) {
        return subscriptionService.getByDeviceId(deviceId).stream()
                .map(SubscriptionResponse::from)
                .toList();
    }

    @PostMapping("/{subscriptionId}/activate")
    SubscriptionResponse activate(@PathVariable UUID subscriptionId) {
        return SubscriptionResponse.from(subscriptionService.activate(subscriptionId));
    }

    @PostMapping("/{subscriptionId}/suspend")
    SubscriptionResponse suspend(@PathVariable UUID subscriptionId) {
        return SubscriptionResponse.from(subscriptionService.suspend(subscriptionId));
    }

    @PostMapping("/{subscriptionId}/resume")
    SubscriptionResponse resume(@PathVariable UUID subscriptionId) {
        return SubscriptionResponse.from(subscriptionService.resume(subscriptionId));
    }

    @PostMapping("/{subscriptionId}/cancel")
    SubscriptionResponse cancel(@PathVariable UUID subscriptionId) {
        return SubscriptionResponse.from(subscriptionService.cancel(subscriptionId));
    }
}
