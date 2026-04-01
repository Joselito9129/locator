import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { APP_CONFIG } from '../../config/config';
import { RutaTiendaPuntoEntity } from '../../interfaces/entities/ruta-tienda-punto.entity';
import { RutaTiendaEntity } from '../../interfaces/entities/ruta-tienda.entity';
import { IGenericResponseEntity } from '../../interfaces/i-generic-response.entity';
import { RutaTiendaPuntoRequest } from '../../interfaces/request/ruta-tienda-punto.request';
import { RutaTiendaRequest } from '../../interfaces/request/ruta-tienda.request';
import { RutaTiendaPuntoModel } from '../../models/ruta-tienda-punto.model';
import { RutaTiendaModel } from '../../models/ruta-tienda.model';
import { RutaTiendaMapperService } from '../mappers/ruta-tienda-mapper.service';

@Injectable({
  providedIn: 'root'
})
export class RutaTiendaService {
  private readonly http = inject(HttpClient);
  private readonly mapper = inject(RutaTiendaMapperService);

  getRutas(): Observable<RutaTiendaModel[]> {
    return this.http.get<IGenericResponseEntity<RutaTiendaEntity[]>>(APP_CONFIG.api.rutaTienda).pipe(
      map(response => this.mapper.mapRutaEntityListToModelList(response.data ?? []))
    );
  }

  getRutaById(id: number): Observable<RutaTiendaModel> {
    return this.http.get<IGenericResponseEntity<RutaTiendaEntity>>(`${APP_CONFIG.api.rutaTienda}/${id}`).pipe(
      map(response => this.mapper.mapRutaEntityToModel(response.data))
    );
  }

  getRutasByTiendaId(tiendaId: number): Observable<RutaTiendaModel[]> {
    return this.http
      .get<IGenericResponseEntity<RutaTiendaEntity[]>>(`${APP_CONFIG.api.rutaTienda}/tienda/${tiendaId}`)
      .pipe(map(response => this.mapper.mapRutaEntityListToModelList(response.data ?? [])));
  }

  createRuta(request: RutaTiendaRequest): Observable<RutaTiendaModel> {
    return this.http.post<IGenericResponseEntity<RutaTiendaEntity>>(APP_CONFIG.api.rutaTienda, request).pipe(
      map(response => this.mapper.mapRutaEntityToModel(response.data))
    );
  }

  updateRuta(id: number, request: RutaTiendaRequest): Observable<RutaTiendaModel> {
    return this.http
      .put<IGenericResponseEntity<RutaTiendaEntity>>(`${APP_CONFIG.api.rutaTienda}/${id}`, request)
      .pipe(map(response => this.mapper.mapRutaEntityToModel(response.data)));
  }

  deleteRuta(id: number): Observable<IGenericResponseEntity<null>> {
    return this.http.delete<IGenericResponseEntity<null>>(`${APP_CONFIG.api.rutaTienda}/${id}`);
  }

  getPuntosByRutaId(rutaTiendaId: number): Observable<RutaTiendaPuntoModel[]> {
    return this.http
      .get<IGenericResponseEntity<RutaTiendaPuntoEntity[]>>(`${APP_CONFIG.api.rutaTiendaPunto}/ruta/${rutaTiendaId}`)
      .pipe(map(response => this.mapper.mapPuntoEntityListToModelList(response.data ?? [])));
  }

  createPunto(request: RutaTiendaPuntoRequest): Observable<RutaTiendaPuntoModel> {
    return this.http
      .post<IGenericResponseEntity<RutaTiendaPuntoEntity>>(APP_CONFIG.api.rutaTiendaPunto, request)
      .pipe(map(response => this.mapper.mapPuntoEntityToModel(response.data)));
  }

  updatePunto(id: number, request: RutaTiendaPuntoRequest): Observable<RutaTiendaPuntoModel> {
    return this.http
      .put<IGenericResponseEntity<RutaTiendaPuntoEntity>>(`${APP_CONFIG.api.rutaTiendaPunto}/${id}`, request)
      .pipe(map(response => this.mapper.mapPuntoEntityToModel(response.data)));
  }

  deletePunto(id: number): Observable<IGenericResponseEntity<null>> {
    return this.http.delete<IGenericResponseEntity<null>>(`${APP_CONFIG.api.rutaTiendaPunto}/${id}`);
  }
}