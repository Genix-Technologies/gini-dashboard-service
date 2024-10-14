package gini.ginidashboardservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ClientProfileV2DTO {

    private Long clientProfileId;
    private Long companyId;
    private Long employeeId;
    private Long salesAgentId;
    private Boolean isActive;
    private Map<String, Object> customerProfile;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
