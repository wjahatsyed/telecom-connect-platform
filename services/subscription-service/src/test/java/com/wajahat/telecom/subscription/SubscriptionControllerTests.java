package com.wajahat.telecom.subscription;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wajahat.telecom.subscription.web.SubscriptionController;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = SubscriptionController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.wajahat\\.telecom\\.subscription\\.(service|repository|web)\\..*"))
class SubscriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndTransitionsSubscription() throws Exception {
        UUID deviceId = UUID.randomUUID();

        String response = mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "deviceId": "%s",
                                  "planCode": "GLOBAL_5GB"
                                }
                                """.formatted(UUID.randomUUID(), deviceId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String subscriptionId = com.jayway.jsonpath.JsonPath.read(response, "$.subscriptionId");

        mockMvc.perform(post("/subscriptions/{subscriptionId}/activate", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(get("/subscriptions/device/{deviceId}", deviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void returnsConflictForInvalidTransition() throws Exception {
        String response = mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "deviceId": "%s",
                                  "planCode": "GLOBAL_5GB"
                                }
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String subscriptionId = com.jayway.jsonpath.JsonPath.read(response, "$.subscriptionId");

        mockMvc.perform(post("/subscriptions/{subscriptionId}/suspend", subscriptionId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
