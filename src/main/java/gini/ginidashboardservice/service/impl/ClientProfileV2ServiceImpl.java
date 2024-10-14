package gini.ginidashboardservice.service.impl;

import gini.ginidashboardservice.dto.ClientProfileDTO;
import gini.ginidashboardservice.dto.ClientProfileV2DTO;
import gini.ginidashboardservice.mapper.ClientProfileMapperV2;
import gini.ginidashboardservice.models.ClientProfile;
import gini.ginidashboardservice.models.ClientProfileV2;
import gini.ginidashboardservice.repositories.ClientProfileV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientProfileV2ServiceImpl {
    @Autowired
    private ClientProfileV2Repository clientProfileRepository;
    @Autowired
    private ClientProfileMapperV2 clientProfileMapper;

    public List<ClientProfileV2> getAllClientProfiles() {
        return clientProfileRepository.findAll();
    }

    public ClientProfileV2 saveClientProfile(Long clientId, ClientProfileV2DTO updatedProfileDTO) {
        if(clientId == null)
        {
            ClientProfileV2 clientProfile = clientProfileMapper.toEntity(updatedProfileDTO);
            clientProfile.setCreatedAt(LocalDateTime.now());
            clientProfile.setLastModifiedAt(LocalDateTime.now());
            return clientProfileRepository.save(clientProfile);
        }
        else
        {
            Optional<ClientProfileV2> existingProfileOpt = clientProfileRepository.findById(clientId);
            if (existingProfileOpt.isPresent()) {
                ClientProfileV2 existingProfile = existingProfileOpt.get();
                clientProfileMapper.updateClientProfileFromDto(updatedProfileDTO, existingProfile);
                existingProfile.setLastModifiedAt(LocalDateTime.now());
                return clientProfileRepository.save(existingProfile);
            } else {
                throw new RuntimeException("Client profile with ID " + clientId + " not found.");
            }
        }
    }

}
