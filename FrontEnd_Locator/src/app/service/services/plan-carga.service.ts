import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { APP_CONFIG } from '../../config/config';
import { PlanCargaResponse } from '../../interfaces/response/plan-carga.response';
import { PlanCargaModel } from '../../models/plan-carga.model';
import { PlanCargaMapperService } from '../mappers/plan-carga-mapper.service';

@Injectable({
  providedIn: 'root'
})
export class PlanCargaService {

  private readonly http = inject(HttpClient);
  private readonly mapper = inject(PlanCargaMapperService);

  getPlanes(): Observable<PlanCargaModel[]> {
    return this.http.get<{ data: PlanCargaResponse[] }>(APP_CONFIG.api.planCarga).pipe(
      map((response) => this.mapper.mapEntityListToModelList(response.data ?? []))
    );
  }

  generarPlan(ventanaId: number): Observable<void> {
    return this.http.post<void>(`${APP_CONFIG.api.planCarga}/generar/${ventanaId}`, {});
  }
}