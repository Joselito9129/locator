import { Injectable } from '@angular/core';
import { RutaGeoEntity } from '../../interfaces/entities/ruta-geo.entity';
import { RutaGeoModel } from '../../models/ruta-geo.model';

@Injectable({
  providedIn: 'root'
})
export class RutaGeoMapperService {

  mapEntityToModel(entity: RutaGeoEntity): RutaGeoModel {
    return {
      id: entity.id,
      tienda: `Tienda ${entity.id}`,
      horaSalidaCentroDistribucion: this.formatDate(entity.fechaCreacion),
      eta: 'Pendiente',
      numeroGuia: entity.guia,
      estado: entity.estado,
      usuarioCreacion: entity.usuarioCreacion,
      latitudOrigen: entity.latitudOrigen,
      longitudOrigen: entity.longitudOrigen,
      latitudDestino: entity.latitudDestino,
      longitudDestino: entity.longitudDestino
    };
  }

  mapEntityListToModelList(entities: RutaGeoEntity[]): RutaGeoModel[] {
    return entities.map((entity: RutaGeoEntity) => this.mapEntityToModel(entity));
  }

  private formatDate(date: string): string {
    if (!date) {
      return '';
    }

    const parsedDate = new Date(date);

    if (Number.isNaN(parsedDate.getTime())) {
      return date;
    }

    return parsedDate.toLocaleString('es-ES', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}