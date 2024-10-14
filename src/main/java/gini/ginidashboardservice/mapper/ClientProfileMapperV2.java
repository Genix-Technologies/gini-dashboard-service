package gini.ginidashboardservice.mapper;

import gini.ginidashboardservice.dto.ClientProfileDTO;
import gini.ginidashboardservice.dto.ClientProfileV2DTO;
import gini.ginidashboardservice.models.ClientProfile;
import gini.ginidashboardservice.models.ClientProfileV2;
import gini.ginidashboardservice.utils.JsonConverter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ClientProfileMapperV2 {
    ClientProfileV2 toEntity(ClientProfileV2DTO dto);

    ClientProfileV2DTO toDto(ClientProfileV2 entity);

    void updateClientProfileFromDto(ClientProfileV2DTO dto, @MappingTarget ClientProfileV2 entity);

    default String mapJsonField(Map<String, Object> value) {
        return value != null ? new JsonConverter().convertToJson(value) : null;
    }

    default Map<String, Object> mapToJsonField(String value) {
        return value != null ? new JsonConverter().convertFromJson(value) : null;
    }
}
