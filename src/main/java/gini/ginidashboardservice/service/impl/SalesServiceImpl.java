package gini.ginidashboardservice.service.impl;

import gini.ginidashboardservice.dto.SalesAgentSummary;
import gini.ginidashboardservice.dto.SalesDashboardResponse;
import gini.ginidashboardservice.models.SalesPipelineEntries;
import gini.ginidashboardservice.repositories.EmployeeGoalRepository;
import gini.ginidashboardservice.repositories.SalesAgentRepository;
import gini.ginidashboardservice.repositories.SalesPipelineEntriesRepository;
import gini.ginidashboardservice.service.SalesService;
import gini.ginidashboardservice.utils.CalculateDates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService{
    private final SalesPipelineEntriesRepository salesPipelineEntryRepository;
    private final EmployeeGoalRepository employeeGoalRepository;
    private final CalculateDates calculateDates;
    private final EmployeeGoalRepository employeeGoalDenormRepository;
    private final SalesAgentRepository salesAgentRepository;

    @Override
    public SalesDashboardResponse getSalesAgentInfo(Long employeeId) {
        // Logic for sales agent-specific data fetching
        BigDecimal currentSales = getTotalTargetPremiumAmountForSalesAgent(employeeId);
        Long totalSales = getDistinctPolicyCountForSalesAgent(employeeId);
        BigDecimal goal = getSalesAgentTargetAmount(employeeId);

        SalesDashboardResponse response = new SalesDashboardResponse();
        response.setCurrentSales(currentSales);
        response.setTotalNumberOfSales(totalSales);
        response.setGoal(goal);

        // Calculate the difference and percentage drop
        BigDecimal difference = currentSales.subtract(goal);
        BigDecimal percentage = BigDecimal.ZERO;
        if (goal.compareTo(BigDecimal.ZERO) > 0) {
            percentage = difference
                    .divide(goal, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        response.setDrop(percentage);

        return response;
    }

    // Fetch the total target premium amount for a sales agent
    private BigDecimal getTotalTargetPremiumAmountForSalesAgent(Long salesAgentId) {
        List<SalesPipelineEntries> entries = salesPipelineEntryRepository.findBySalesAgentIdAndCreatedDtBetweenAndStage(
                salesAgentId,
                calculateDates.getStartDate(),
                calculateDates.getEndDate(),
                "Closed Won"
        );

        return entries.stream()
                .map(SalesPipelineEntries::getTargetPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Fetch the distinct policy count for a sales agent
    private Long getDistinctPolicyCountForSalesAgent(Long salesAgentId) {
        return salesPipelineEntryRepository.countDistinctPolicyNoBySalesAgentIdAndCreatedDtBetweenAndStage(
                salesAgentId,
                calculateDates.getStartDate(),
                calculateDates.getEndDate(),
                "Closed Won"
        );
    }

    // Fetch the goal for a sales agent
    private BigDecimal getSalesAgentTargetAmount(Long salesAgentId) {
//        return salesAgentGoalRepository.calculateSumOfGoalsBySalesAgentId(salesAgentId);
        return new BigDecimal(114000000.00);
    }


    @Override
    public SalesDashboardResponse getSalesInfo(Long employeeId) {
        // Call each method synchronously
        BigDecimal currentSales = getTotalTargetPremiumAmountByEmployeeIdAndCurrentYear(employeeId);
        Long totalSales = getDistinctPolicyCountByEmployeeIdAndYear(employeeId);
        BigDecimal goal = getTargetAmountByEmployeeId(employeeId);

        // Create and populate the response object
        SalesDashboardResponse response = new SalesDashboardResponse();
        response.setCurrentSales(currentSales);
        response.setTotalNumberOfSales(totalSales);
        response.setGoal(goal);

        // Calculate the difference and percentage drop
        BigDecimal difference = currentSales.subtract(goal);
        BigDecimal percentage = BigDecimal.ZERO;
        if (goal.compareTo(BigDecimal.ZERO) > 0) {
            percentage = difference
                    .divide(goal, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        response.setDrop(percentage);

        return response;
    }

    private BigDecimal getTotalTargetPremiumAmountByEmployeeIdAndCurrentYear(Long employeeId) {
        // Fetch all the sales pipeline entries based on the criteria
        List<SalesPipelineEntries> entries = salesPipelineEntryRepository.findByEmployeeIdAndCreatedDtBetweenAndStage(
                employeeId,
                calculateDates.getStartDate(),
                calculateDates.getEndDate(),
                "Closed Won");

        // Calculate the total target premium amount
        return entries.stream()
                .map(SalesPipelineEntries::getTargetPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private Long getDistinctPolicyCountByEmployeeIdAndYear(Long employeeId) {
        // Fetch the count of distinct policy numbers synchronously
        return salesPipelineEntryRepository.countDistinctPolicyNoByEmployeeIdAndCreatedDtBetweenAndStage(
                employeeId,
                calculateDates.getStartDate(),
                calculateDates.getEndDate(),
                "Closed Won");
    }

    private BigDecimal getTargetAmountByEmployeeId(Long employeeId) {
        return employeeGoalRepository.calculateSumOfGoalsByEmployeeId(employeeId);
    }

@Override
    public List<SalesAgentSummary> getTopSalesAgents(String agentName) {
    List<Object[]> results = salesAgentRepository.findTopSalesAgents(agentName);
    List<SalesAgentSummary> salesAgentSummaries = new ArrayList<>();

    for (Object[] result : results) {
        String name = (String) result[0];
        String email = (String) result[1];
        BigDecimal totalTargetPremium = (BigDecimal) result[2];

        salesAgentSummaries.add(new SalesAgentSummary(name, email, totalTargetPremium));
    }

    return salesAgentSummaries;
}
}
