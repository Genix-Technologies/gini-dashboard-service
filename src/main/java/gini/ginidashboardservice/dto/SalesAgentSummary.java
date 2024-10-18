package gini.ginidashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesAgentSummary {
    private Long agentId;
    private String agentName;
    private String email;
    private BigDecimal totalTargetPremium;
}
