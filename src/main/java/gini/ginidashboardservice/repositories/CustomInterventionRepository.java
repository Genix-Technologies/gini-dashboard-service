package gini.ginidashboardservice.repositories;

import gini.ginidashboardservice.dto.InterventionResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class CustomInterventionRepository {
    @Value("${activity.intervention.interval.days}")
    private int intervalDays;
    @PersistenceContext
    EntityManager entityManager;

    public Page<InterventionResponse> findInterventionTypeCountsByEmployeeId(Long employeeId, Pageable pageable) {
        // Construct the SQL query
        String sql = "SELECT employee_id,a.interaction_type AS type, COUNT(*) AS count FROM interactions a " +
                "WHERE a.employee_id = :employeeId " +
                "AND a.last_modified_at BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL :intervalDays DAY) AND CURRENT_DATE() " +
                "GROUP BY type " +
                "ORDER BY count DESC";
        // Execute the query for the results
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("employeeId", employeeId);
        query.setParameter("intervalDays", intervalDays);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();

        // Count total number of records for pagination
        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(DISTINCT a.interaction_type) FROM interactions a " +
                        "WHERE a.employee_id = :employeeId " +
                        "AND a.last_modified_at BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL :intervalDays DAY) AND CURRENT_DATE()");
        countQuery.setParameter("employeeId", employeeId);
        countQuery.setParameter("intervalDays", intervalDays);
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // Convert results to DTOs
        List<InterventionResponse> interventionResponses = new ArrayList<>();
        for (Object[] result : results) {
            InterventionResponse response = new InterventionResponse();
            response.setEmployeeId(((Number) result[0]).longValue());
            response.setInterventionType((String) result[1]);
            response.setCount(((Number) result[2]).longValue());
            interventionResponses.add(response);
        }
        // Return the page with results
        return new PageImpl<>(interventionResponses, pageable, total);
    }

    public Page<Object[]> findInteractionsWithAgentNamesByEmployeeId(Long employeeId, Pageable pageable) {
        // Construct the SQL query
        String sql = "SELECT i.description, sa.agent_name " +
                "FROM interactions i " +
                "JOIN sales_agents sa ON i.sales_agent_id = sa.sales_agent_id " +
                "WHERE i.employee_id = :employeeId " +
                "AND i.last_modified_at BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL :intervalDays DAY) AND CURRENT_DATE() " +
                "ORDER BY i.last_modified_at DESC";

        // Execute the query
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("employeeId", employeeId);
        query.setParameter("intervalDays", intervalDays);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();

        // Count total records for pagination
        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM interactions i " +
                        "WHERE i.employee_id = :employeeId " +
                        "AND i.last_modified_at BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL :intervalDays DAY) AND CURRENT_DATE()");
        countQuery.setParameter("employeeId", employeeId);
        countQuery.setParameter("intervalDays", intervalDays);
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // Return paginated result
        return new PageImpl<>(results, pageable, total);
    }
}
