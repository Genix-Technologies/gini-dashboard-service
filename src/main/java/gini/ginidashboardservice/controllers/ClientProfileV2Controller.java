package gini.ginidashboardservice.controllers;

import gini.ginidashboardservice.dto.ClientProfileDTO;
import gini.ginidashboardservice.dto.ClientProfileV2DTO;
import gini.ginidashboardservice.models.ClientProfile;
import gini.ginidashboardservice.models.ClientProfileV2;
import gini.ginidashboardservice.service.ClientProfileService;
import gini.ginidashboardservice.service.impl.ClientProfileV2ServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2")
public class ClientProfileV2Controller {

    private final ClientProfileV2ServiceImpl clientProfileService;

    public ClientProfileV2Controller(ClientProfileV2ServiceImpl clientProfileService) {
        this.clientProfileService = clientProfileService;
    }

    @GetMapping("/client-profiles")
    public List<ClientProfileV2> getAllClientProfiles() {
        return clientProfileService.getAllClientProfiles();
    }

    @PutMapping("/client-profiles/{clientId}")
    public ResponseEntity<ClientProfileV2> updateClientProfile(
            @PathVariable Long clientId,
            @RequestBody ClientProfileV2DTO updatedProfile) {

        ClientProfileV2 updatedClientProfile = clientProfileService.saveClientProfile(clientId, updatedProfile);
        return ResponseEntity.ok(updatedClientProfile);
    }
    @PostMapping("/client-profiles")
    public ResponseEntity<ClientProfileV2> createClientProfile(
            @RequestBody ClientProfileV2DTO updatedProfile) {

        ClientProfileV2 updatedClientProfile = clientProfileService.saveClientProfile(null, updatedProfile);
        return ResponseEntity.ok(updatedClientProfile);
    }
}