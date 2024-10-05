package gini.ginidashboardservice.service.impl;

import gini.ginidashboardservice.dto.InterventionResponse;
import gini.ginidashboardservice.dto.PeopleDTO;
import gini.ginidashboardservice.repositories.CustomInterventionRepository;
import gini.ginidashboardservice.repositories.SalesAgentRepository;
import gini.ginidashboardservice.service.InterventionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterventionServiceImpl implements InterventionService {
    private final SalesAgentRepository salesAgentRepository;
    private final CustomInterventionRepository customInterventionRepository;

    public InterventionServiceImpl(SalesAgentRepository salesAgentRepository, CustomInterventionRepository customInterventionRepository) {
        this.salesAgentRepository = salesAgentRepository;
        this.customInterventionRepository = customInterventionRepository;
    }


    @Override
    public Page<InterventionResponse> getInterventionTypeCountsByEmployeeId(Long employeeId, Pageable pageable) {
        return customInterventionRepository.findInterventionTypeCountsByEmployeeId(employeeId, pageable);
    }

    @Override
    public Page<PeopleDTO> getInteractionsWithAgentNames(Long employeeId, Pageable pageable) {
        Page<Object[]> results = customInterventionRepository.findInteractionsWithAgentNamesByEmployeeId(employeeId, pageable);

        List<PeopleDTO> peopleDTOS = new ArrayList<>();
        for (Object[] result : results) {
            String description = (String) result[0];
            String agentName = (String) result[1];

            PeopleDTO dto = new PeopleDTO(description, agentName);
            peopleDTOS.add(dto);
        }

        return new PageImpl<>(peopleDTOS, pageable, results.getTotalElements());
    }
}
