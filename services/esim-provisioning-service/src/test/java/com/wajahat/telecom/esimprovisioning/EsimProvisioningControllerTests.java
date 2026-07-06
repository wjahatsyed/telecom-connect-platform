package com.wajahat.telecom.esimprovisioning;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import com.wajahat.telecom.esimprovisioning.repository.EsimProfileRepository;
import com.wajahat.telecom.esimprovisioning.service.EsimProvisioningService;
import com.wajahat.telecom.esimprovisioning.web.EsimProvisioningController;
import java.util.LinkedHashMap;
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

@WebMvcTest(EsimProvisioningController.class)
@Import({EsimProvisioningService.class, EsimProvisioningControllerTests.TestConfig.class})
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

    @TestConfiguration
    static class TestConfig {

        @Bean
        EsimProfileRepository esimProfileRepository() {
            return new EsimProfileRepository() {
                private final java.util.Map<String, EsimProfile> profiles = new LinkedHashMap<>();

                @Override
                public EsimProfile save(EsimProfile profile) {
                    profiles.put(profile.iccid(), profile);
                    return profile;
                }

                @Override
                public Optional<EsimProfile> findByIccid(String iccid) {
                    return Optional.ofNullable(profiles.get(iccid));
                }
            };
        }
    }
}
