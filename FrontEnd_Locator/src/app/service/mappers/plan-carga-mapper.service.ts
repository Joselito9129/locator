import { Injectable } from '@angular/core';

import { PlanCargaResponse, PlanCargaTiendaResponse } from '../../interfaces/response/plan-carga.response';
import { PlanCargaModel, PlanCargaTiendaModel } from '../../models/plan-carga.model';

@Injectable({
  providedIn: 'root'
})
export class PlanCargaMapperService {

  mapEntityToModel(entity: PlanCargaResponse): PlanCargaModel {
    return {
      idPlanCamion: entity.idPlanCamion,
      planId: entity.planId,
      tipoCamion: entity.tipoCamion,
      capacidadPeso: entity.capacidadPeso,
      capacidadVolumen: entity.capacidadVolumen,
      pesoActual: entity.pesoActual,
      volumenActual: entity.volumenActual,
      porcentajeOcupacion: entity.porcentajeOcupacion,
      estado: entity.estado,
      estadoVisual: this.getEstado(entity.porcentajeOcupacion),
      tiendas: this.mapTiendas(entity.tiendas ?? [])
    };
  }

  mapEntityListToModelList(entities: PlanCargaResponse[]): PlanCargaModel[] {
    return entities.map((entity) => this.mapEntityToModel(entity));
  }

  private mapTiendas(entities: PlanCargaTiendaResponse[]): PlanCargaTiendaModel[] {
    return entities.map((item) => ({
      tiendaId: item.tiendaId,
      pesoAsignado: item.pesoAsignado,
      volumenAsignado: item.volumenAsignado
    }));
  }

  private getEstado(ocupacion: number): 'OK' | 'WARNING' | 'ERROR' {
    if (ocupacion >= 0.8) {
      return 'OK';
    }

    if (ocupacion >= 0.5) {
      return 'WARNING';
    }

    return 'ERROR';
  }
}