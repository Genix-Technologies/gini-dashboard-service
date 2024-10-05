package gini.ginidashboardservice.service;

import gini.ginidashboardservice.dto.SalesAgentSummary;
import gini.ginidashboardservice.dto.SalesDashboardResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalesService {
    SalesDashboardResponse getSalesInfo(Long employeeId);
    public List<SalesAgentSummary> getTopSalesAgents(String agentName);

    SalesDashboardResponse getSalesAgentInfo(Long id);
}
