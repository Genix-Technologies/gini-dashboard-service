package gini.ginidashboardservice.repositories;


import gini.ginidashboardservice.dto.ActivityResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ActivityRepository{
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ActivityResponse> findActivityTypeCountsByEmployeeId(Long employeeId, Pageable pageable) {
        // Construct the SQL query
        String sql = "SELECT a.activity_type AS activityType, COUNT(*) AS count FROM activities a " +
                "WHERE a.employee_id = :employeeId " +
                "AND a.last_modified_dt BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) AND CURRENT_DATE() " +
                "GROUP BY a.activity_type " +
                "ORDER BY count DESC";

        // Execute the query for the results
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("employeeId", employeeId);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();

        // Count total number of records for pagination
        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(DISTINCT a.activity_type) FROM activities a " +
                        "WHERE a.employee_id = :employeeId " +
                        "AND a.last_modified_dt BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) AND CURRENT_DATE()");
        countQuery.setParameter("employeeId", employeeId);
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // Convert results to DTOs
        List<ActivityResponse> activityResponses = new ArrayList<>();
        for (Object[] result : results) {
            ActivityResponse response = new ActivityResponse();
            response.setActivityType((String) result[0]);
            response.setCount(((Number) result[1]).longValue());
            activityResponses.add(response);
        }
        // Return the page with results
        return new PageImpl<>(activityResponses, pageable, total);
    }
}