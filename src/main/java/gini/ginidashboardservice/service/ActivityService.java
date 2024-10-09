package gini.ginidashboardservice.service;

import gini.ginidashboardservice.dto.ActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ActivityService {
    public Page<ActivityResponse> getActivityTypeCountsByEmployeeId(Long employeeId, Pageable pageable);

    Page<ActivityResponse> getActivityTypeCountsByAgentId(Long id, Pageable pageable);
}
