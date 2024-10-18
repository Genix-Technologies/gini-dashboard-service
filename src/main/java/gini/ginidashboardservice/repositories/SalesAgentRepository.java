package gini.ginidashboardservice.repositories;

import gini.ginidashboardservice.dto.SalesAgentSummary;
import gini.ginidashboardservice.models.Intervention;
import gini.ginidashboardservice.models.SalesAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesAgentRepository extends JpaRepository<SalesAgent, Long> {
    List<Intervention> findBySalesAgentId(Long salesAgentId);

    @Query(value = "SELECT sa.sales_agent_id,sa.agent_name AS agentName, sa.email AS email, SUM(spe.target_premium_amount) AS totalTargetPremium " +
            "FROM sales_pipeline_entries spe " +
            "JOIN sales_agents sa ON spe.sales_agent_id = sa.sales_agent_id " +
            "WHERE YEAR(spe.created_dt) = YEAR(CURRENT_DATE) " +
            "AND spe.stage = 'closed won' " +
            "AND (:agentName IS NULL OR sa.agent_name LIKE %:agentName%) " + // Filter by name
            "GROUP BY sa.agent_name, sa.email " +
            "ORDER BY totalTargetPremium DESC",
            nativeQuery = true)
    List<Object[]> findTopSalesAgents(@Param("agentName") String agentName);
}