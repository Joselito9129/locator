import { Injectable } from '@angular/core';

import { RutaTiendaPuntoEntity } from '../../interfaces/entities/ruta-tienda-punto.entity';
import { RutaTiendaEntity } from '../../interfaces/entities/ruta-tienda.entity';
import { RutaTiendaPuntoModel } from '../../models/ruta-tienda-punto.model';
import { RutaTiendaModel } from '../../models/ruta-tienda.model';

@Injectable({
  providedIn: 'root'
})
export class RutaTiendaMapperService {

  mapRutaEntityToModel(entity: RutaTiendaEntity): RutaTiendaModel {
    return {
      id: entity.id,
      tiendaId: entity.tiendaId,
      estado: entity.estado,
      usuarioCreacion: entity.usuarioCreacion,
      fechaCreacion: entity.fechaCreacion,
      usuarioActualizacion: entity.usuarioActualizacion,
      fechaActualizacion: entity.fechaActualizacion,
      fechaCreacionLabel: this.formatDate(entity.fechaCreacion),
      fechaActualizacionLabel: this.formatDate(entity.fechaActualizacion)
    };
  }

  mapRutaEntityListToModelList(entities: RutaTiendaEntity[]): RutaTiendaModel[] {
    return entities.map(entity => this.mapRutaEntityToModel(entity));
  }

  mapPuntoEntityToModel(entity: RutaTiendaPuntoEntity): RutaTiendaPuntoModel {
    return {
      id: entity.id,
      rutaTiendaId: entity.rutaTiendaId,
      orden: entity.orden,
      estado: entity.estado,
      usuarioCreacion: entity.usuarioCreacion,
      fechaCreacion: entity.fechaCreacion,
      usuarioActualizacion: entity.usuarioActualizacion,
      fechaActualizacion: entity.fechaActualizacion,
      fechaCreacionLabel: this.formatDate(entity.fechaCreacion),
      fechaActualizacionLabel: this.formatDate(entity.fechaActualizacion)
    };
  }

  mapPuntoEntityListToModelList(entities: RutaTiendaPuntoEntity[]): RutaTiendaPuntoModel[] {
    return entities
      .slice()
      .sort((a, b) => a.orden - b.orden)
      .map(entity => this.mapPuntoEntityToModel(entity));
  }

  private formatDate(value: string | null): string {
    if (!value) {
      return '-';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return value;
    }

    return new Intl.DateTimeFormat('es-ES', {
      dateStyle: 'short',
      timeStyle: 'short'
    }).format(date);
  }
}