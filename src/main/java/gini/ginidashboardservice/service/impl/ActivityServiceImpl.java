package gini.ginidashboardservice.service.impl;

import gini.ginidashboardservice.dto.ActivityResponse;
import gini.ginidashboardservice.repositories.ActivityRepository;
import gini.ginidashboardservice.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Page<ActivityResponse> getActivityTypeCountsByEmployeeId(Long employeeId, Pageable pageable) {
        return activityRepository.findActivityTypeCountsByEmployeeId(employeeId, pageable);
    }

    @Override
    public Page<ActivityResponse> getActivityTypeCountsByAgentId(Long id, Pageable pageable)
    {
        return activityRepository.getActivityTypeCountsBySalesAgentId(id, pageable);
    }
}
