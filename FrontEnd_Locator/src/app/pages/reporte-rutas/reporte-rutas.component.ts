import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Observable, Subject, of } from 'rxjs';
import { catchError, map, shareReplay, startWith, switchMap } from 'rxjs/operators';

import { RutaGeoModel } from '../../models/ruta-geo.model';
import { RutaGeoService } from '../../service/services/ruta-geo.service';

interface ReporteRutasState {
  rutas: RutaGeoModel[];
  loading: boolean;
  errorMessage: string;
}

@Component({
  selector: 'app-reporte-rutas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reporte-rutas.component.html',
  styleUrl: './reporte-rutas.component.scss'
})
export class ReporteRutasComponent {
  private readonly rutaGeoService = inject(RutaGeoService);

  private readonly reloadSubject = new Subject<void>();

  readonly state$: Observable<ReporteRutasState> = this.reloadSubject.pipe(
    startWith(void 0),
    switchMap(() =>
      this.rutaGeoService.getRutasGeo().pipe(
        map((rutas: RutaGeoModel[]) => ({
          rutas,
          loading: false,
          errorMessage: ''
        })),
        startWith({
          rutas: [],
          loading: true,
          errorMessage: ''
        }),
        catchError(() =>
          of({
            rutas: [],
            loading: false,
            errorMessage: 'Ocurrió un error al consultar la información.'
          })
        )
      )
    ),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  loadRutas(): void {
    this.reloadSubject.next();
  }

  trackById(index: number, item: RutaGeoModel): number {
    return item.id;
  }

  refreshFromWs(): void {
    this.reloadSubject.next();
  }
}