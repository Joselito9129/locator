import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { APP_CONFIG } from '../../config/config';
import { RutaGeoResponse } from '../../interfaces/response/ruta-geo.response';
import { RutaGeoModel } from '../../models/ruta-geo.model';
import { RutaGeoMapperService } from '../mappers/ruta-geo-mapper.service';

@Injectable({
  providedIn: 'root'
})
export class RutaGeoService {
  private readonly http = inject(HttpClient);
  private readonly mapper = inject(RutaGeoMapperService);

  getRutasGeo(): Observable<RutaGeoModel[]> {
    return this.http.get<RutaGeoResponse>(APP_CONFIG.api.rutasGeo).pipe(
      map((response: RutaGeoResponse) =>
        this.mapper.mapEntityListToModelList(response.data ?? [])
      )
    );
  }
}