package gini.ginidashboardservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "sales_agents", schema = "axonai_demo")

public class SalesAgent {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "sales_agent_id")
        private Long salesAgentId;

        @Column(name = "company_id", nullable = false)
        private Long companyId;

        @Column(name = "distributor_id")
        private Long distributorId;

        @Column(name = "agent_name", nullable = false)
        private String agentName;

        @Column(name = "license_no", nullable = false)
        private String licenseNo;

        @Column(name = "agent_type", nullable = false)
        private String agentType;

        @Column(name = "territory", nullable = false)
        private String territory;

        @Column(name = "email")
        private String email;

        @Column(name = "address")
        private String address;

        @Column(name = "phone")
        private String phone;

        @Column(name = "ms_teams")
        private String msTeams;

        @Column(name = "performance_segment", nullable = false)
        private String performanceSegment;

        @Column(name = "prev_year")
        private Integer prevYear;

        @Column(name = "prev_year_sales_amount")
        private BigDecimal prevYearSalesAmount;

        @Column(name = "lifetime_value")
        private BigDecimal lifetimeValue;

        @Column(name = "engagement_score")
        private Integer engagementScore;

        @Column(name = "60_day_sentiment")
        private Integer sentiment60Day;

        @Column(name = "30_day_sales_forecast")
        private BigDecimal salesForecast30Day;

        @Column(name = "strengths")
        private String strengths;

        @Column(name = "topics")
        private String topics;

        @Column(name = "products", columnDefinition = "json")
        private String products;

        @Column(name = "created_at", nullable = false, updatable = false)
        private Timestamp createdAt;

        @Column(name = "last_modified_at", nullable = false)
        private Timestamp lastModifiedAt;
}