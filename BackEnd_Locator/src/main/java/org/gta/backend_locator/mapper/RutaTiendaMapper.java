package org.gta.backend_locator.mapper;

import org.gta.backend_locator.dto.request.RutaTiendaRequest;
import org.gta.backend_locator.dto.response.RutaTiendaResponse;
import org.gta.backend_locator.model.RutaTienda;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RutaTiendaMapper {

    RutaTienda toEntity(RutaTiendaRequest request);

    RutaTiendaResponse toResponse(RutaTienda entity);

    List<RutaTiendaResponse> toResponseList(List<RutaTienda> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(RutaTiendaRequest request, @MappingTarget RutaTienda entity);
}