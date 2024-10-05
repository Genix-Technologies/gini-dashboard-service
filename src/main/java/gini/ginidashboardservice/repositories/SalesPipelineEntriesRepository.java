package gini.ginidashboardservice.repositories;

import gini.ginidashboardservice.dto.StageSummary;
import gini.ginidashboardservice.models.SalesPipelineEntries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface SalesPipelineEntriesRepository extends JpaRepository<SalesPipelineEntries, Long> {
    List<SalesPipelineEntries> findByEmployeeIdAndCreatedDtBetweenAndStage(Long employeeId, LocalDateTime startDate, LocalDateTime endDate, String closedWon);

    Long countDistinctPolicyNoByEmployeeIdAndCreatedDtBetweenAndStage(Long employeeId, LocalDateTime startDate, LocalDateTime endDate, String closedWon);

    List<SalesPipelineEntries> findByEmployeeIdAndCreatedDtBetweenAndOpportunityTypeInAndStageIn(Long employeeId, LocalDateTime startDate, LocalDateTime endDate, String[] opportunityType, String[] stages);

    List<SalesPipelineEntries> findBySalesAgentIdAndCreatedDtBetweenAndStage(Long salesAgentId, LocalDateTime startDate, LocalDateTime endDate, String closedWon);

    Long countDistinctPolicyNoBySalesAgentIdAndCreatedDtBetweenAndStage(Long salesAgentId, LocalDateTime startDate, LocalDateTime endDate, String closedWon);

    List<SalesPipelineEntries> findBySalesAgentIdAndCreatedDtBetweenAndOpportunityTypeInAndStageIn(
            Long salesAgentId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String[] opportunityType,
            String[] stages);

    @Query(value = "SELECT stage, COUNT(policy_no) AS policy_count, SUM(target_premium_amount) AS premium_sum " +
            "FROM sales_pipeline_entries " +
            "WHERE employee_id = :employeeId AND YEAR(created_dt) = YEAR(CURRENT_DATE) " +
            "GROUP BY stage",
            nativeQuery = true)
    List<Object[]> findPoliciesCountAndPremiumSumByStageForEmployee(Long employeeId);
}
