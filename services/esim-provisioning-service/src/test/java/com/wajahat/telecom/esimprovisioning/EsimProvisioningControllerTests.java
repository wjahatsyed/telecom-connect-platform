package com.wajahat.telecom.esimprovisioning;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wajahat.telecom.esimprovisioning.web.EsimProvisioningController;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = EsimProvisioningController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.wajahat\\.telecom\\.esimprovisioning\\.(service|repository|web)\\..*"))
class EsimProvisioningControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void provisionsAndActivatesEsimProfile() throws Exception {
        String response = mockMvc.perform(post("/esims/provision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "deviceId": "%s",
                                  "subscriptionId": "%s"
                                }
                                """.formatted(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PROVISIONED"))
                .andExpect(jsonPath("$.iccid").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String iccid = com.jayway.jsonpath.JsonPath.read(response, "$.iccid");

        mockMvc.perform(get("/esims/{iccid}", iccid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iccid").value(iccid));

        mockMvc.perform(post("/esims/{iccid}/activate", iccid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void returnsConflictForInvalidTransition() throws Exception {
        String response = mockMvc.perform(post("/esims/provision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "deviceId": "%s",
                                  "subscriptionId": "%s"
                                }
                                """.formatted(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String iccid = com.jayway.jsonpath.JsonPath.read(response, "$.iccid");

        mockMvc.perform(post("/esims/{iccid}/suspend", iccid))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
