package com.wajahat.telecom.esimprovisioning.web;

import com.wajahat.telecom.esimprovisioning.dto.EsimProfileResponse;
import com.wajahat.telecom.esimprovisioning.dto.ProvisionEsimRequest;
import com.wajahat.telecom.esimprovisioning.service.EsimProvisioningService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/esims")
public class EsimProvisioningController {

    private final EsimProvisioningService esimProvisioningService;

    EsimProvisioningController(EsimProvisioningService esimProvisioningService) {
        this.esimProvisioningService = esimProvisioningService;
    }

    @PostMapping("/provision")
    ResponseEntity<EsimProfileResponse> provision(@Valid @RequestBody ProvisionEsimRequest request) {
        EsimProfileResponse response = EsimProfileResponse.from(esimProvisioningService.provision(request));
        return ResponseEntity.created(URI.create("/esims/" + response.iccid())).body(response);
    }

    @GetMapping("/{iccid}")
    EsimProfileResponse getByIccid(@PathVariable String iccid) {
        return EsimProfileResponse.from(esimProvisioningService.getByIccid(iccid));
    }

    @PostMapping("/{iccid}/activate")
    EsimProfileResponse activate(@PathVariable String iccid) {
        return EsimProfileResponse.from(esimProvisioningService.activate(iccid));
    }

    @PostMapping("/{iccid}/suspend")
    EsimProfileResponse suspend(@PathVariable String iccid) {
        return EsimProfileResponse.from(esimProvisioningService.suspend(iccid));
    }

    @PostMapping("/{iccid}/terminate")
    EsimProfileResponse terminate(@PathVariable String iccid) {
        return EsimProfileResponse.from(esimProvisioningService.terminate(iccid));
    }
}
