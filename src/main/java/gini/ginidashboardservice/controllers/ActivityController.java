package gini.ginidashboardservice.controllers;

import gini.ginidashboardservice.dto.ActivityResponse;
import gini.ginidashboardservice.dto.PipelineDashboardResponse;
import gini.ginidashboardservice.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public ResponseEntity<Page<ActivityResponse>> getActivityTypeCounts(
            @RequestHeader("X-Username") Long loggedInEmployeeId,
            @RequestParam Long id,
            @RequestParam(value = "userType", defaultValue = "employee") String userType,
            @RequestParam int page,
            @RequestParam int size) {

        Page<ActivityResponse> response;
        if ("sales_agent".equalsIgnoreCase(userType)) {
            response = activityService.getActivityTypeCountsByAgentId(id, PageRequest.of(page, size));  // Call sales agent-specific service
        } else {
            if (!id.equals(loggedInEmployeeId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            response = activityService.getActivityTypeCountsByEmployeeId(id, PageRequest.of(page, size));  // Call employee-specific service
        }
        return ResponseEntity.ok(response);
    }
}