package org.gta.backend_locator.mapper;

import org.gta.backend_locator.dto.request.RutaTiendaPuntoRequest;
import org.gta.backend_locator.dto.response.RutaTiendaPuntoResponse;
import org.gta.backend_locator.model.RutaTiendaPunto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RutaTiendaPuntoMapper {

    RutaTiendaPunto toEntity(RutaTiendaPuntoRequest request);

    RutaTiendaPuntoResponse toResponse(RutaTiendaPunto entity);

    List<RutaTiendaPuntoResponse> toResponseList(List<RutaTiendaPunto> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(RutaTiendaPuntoRequest request, @MappingTarget RutaTiendaPunto entity);
}
