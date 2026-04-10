package org.gta.backend_locator.mapper;

import org.gta.backend_locator.dto.request.RutaGeoRequest;
import org.gta.backend_locator.dto.response.RutaGeoResponse;
import org.gta.backend_locator.model.RutaGeo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RutaGeoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    RutaGeo toEntity(RutaGeoRequest request);

    @Mapping(target = "etainicio", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "etaactual", expression = "java(java.time.LocalDateTime.now().plusMinutes(30))")
    RutaGeoResponse toResponse(RutaGeo entity);

}
