package gini.ginidashboardservice.service.impl;

import gini.ginidashboardservice.dto.PipelineDashboardResponse;
import gini.ginidashboardservice.dto.StageSummary;
import gini.ginidashboardservice.models.SalesPipelineEntries;
import gini.ginidashboardservice.repositories.SalesPipelineEntriesRepository;
import gini.ginidashboardservice.service.PipelineService;
import gini.ginidashboardservice.utils.CalculateDates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PipelineServiceImpl implements PipelineService{
    private final SalesPipelineEntriesRepository salesPipelineEntryRepository;
    private final CalculateDates calculateDates;

    public PipelineServiceImpl(SalesPipelineEntriesRepository salesPipelineEntryRepository, CalculateDates calculateDates) {
        this.salesPipelineEntryRepository = salesPipelineEntryRepository;
        this.calculateDates = calculateDates;
    }

    @Override
    public PipelineDashboardResponse getPipelineInfo(Long employeeId) {
        BigDecimal pipelineSales = getPipelineSalesByEmployeeIdAndCurrentYear(employeeId);
        PipelineDashboardResponse response = new PipelineDashboardResponse();
        response.setPipelineSales(pipelineSales);
        return response;
    }

    private BigDecimal getPipelineSalesByEmployeeIdAndCurrentYear(Long employeeId) {
        String[] opportunityType = {"New Policy Sale", "Cross-Sell Opportunity"};
        String[] stages = {"Qualification", "Underwriting", "Proposal", "Negotiation", "Needs Analysis", "Prospecting"};

        List<SalesPipelineEntries> entries = salesPipelineEntryRepository
                .findByEmployeeIdAndCreatedDtBetweenAndOpportunityTypeInAndStageIn(
                        employeeId,
                        calculateDates.getStartDate(),
                        calculateDates.getEndDate(),
                        opportunityType,
                        stages);

        // Calculate the total target premium amount
        return entries.stream()
                .map(SalesPipelineEntries::getTargetPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<StageSummary> getPoliciesCountAndPremiumSumByStageForEmployee(Long employeeId) {
        List<Object[]> results = salesPipelineEntryRepository.findPoliciesCountAndPremiumSumByStageForEmployee(employeeId);

        return results.stream().map(result -> new StageSummary(
                (String) result[0],               // stage
                ((Number) result[1]).longValue(),  // policy_count
                ((BigDecimal) result[2]) // premium_sum
        )).collect(Collectors.toList());
    }

    @Override
    public PipelineDashboardResponse getPipelineInfoForSalesAgent(Long salesAgentId) {
        BigDecimal pipelineSales = getPipelineSalesBySalesAgentIdAndCurrentYear(salesAgentId);
        PipelineDashboardResponse response = new PipelineDashboardResponse();
        response.setPipelineSales(pipelineSales);
        return response;
    }

    private BigDecimal getPipelineSalesBySalesAgentIdAndCurrentYear(Long salesAgentId) {
        String[] opportunityType = {"New Policy Sale", "Cross-Sell Opportunity"};
        String[] stages = {"Qualification", "Underwriting", "Proposal", "Negotiation", "Needs Analysis", "Prospecting"};

        // Fetch the sales pipeline entries for the sales agent
        List<SalesPipelineEntries> entries = salesPipelineEntryRepository
                .findBySalesAgentIdAndCreatedDtBetweenAndOpportunityTypeInAndStageIn(
                        salesAgentId,
                        calculateDates.getStartDate(),
                        calculateDates.getEndDate(),
                        opportunityType,
                        stages);

        // Calculate the total target premium amount for sales agents
        return entries.stream()
                .map(SalesPipelineEntries::getTargetPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
