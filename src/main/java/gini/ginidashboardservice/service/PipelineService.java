package gini.ginidashboardservice.service;

import gini.ginidashboardservice.dto.PipelineDashboardResponse;
import gini.ginidashboardservice.dto.StageSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PipelineService {
    PipelineDashboardResponse getPipelineInfo(Long employeeId);
    List<StageSummary> getPoliciesCountAndPremiumSumByStageForEmployee(Long employeeId);

    PipelineDashboardResponse getPipelineInfoForSalesAgent(Long id);
}
