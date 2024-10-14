package gini.ginidashboardservice.models;

import gini.ginidashboardservice.utils.JsonConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "client_profiles_v2")
@Data
public class ClientProfileV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientProfileId;
    private Long companyId;
    private Long employeeId;
    private Long salesAgentId;
    private Boolean isActive;
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> customerProfile;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
