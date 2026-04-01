import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { BehaviorSubject, catchError, map, of, shareReplay, startWith, switchMap, take } from 'rxjs';

import { PlanCargaModel } from '../../models/plan-carga.model';
import { PlanCargaService } from '../../service/services/plan-carga.service';

interface DataState<T> {
  data: T;
  loading: boolean;
  errorMessage: string | null;
}

@Component({
  selector: 'app-plan-carga',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './plan-carga.component.html',
  styleUrl: './plan-carga.component.scss'
})
export class PlanCargaComponent implements OnInit {

  private readonly planCargaService = inject(PlanCargaService);
  private readonly refresh$ = new BehaviorSubject<void>(undefined);

  expandedCamionId: number | null = null;

  readonly state$ = this.refresh$.pipe(
    switchMap(() =>
      this.planCargaService.getPlanes().pipe(
        map((data): DataState<PlanCargaModel[]> => ({
          data,
          loading: false,
          errorMessage: null
        })),
        startWith({
          data: [],
          loading: true,
          errorMessage: null
        } as DataState<PlanCargaModel[]>),
        catchError(() =>
          of({
            data: [],
            loading: false,
            errorMessage: 'No fue posible cargar la planificación.'
          } as DataState<PlanCargaModel[]>)
        )
      )
    ),
    shareReplay(1)
  );

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.refresh$.next();
  }

  generar(): void {
    this.planCargaService.generarPlan(1).pipe(take(1)).subscribe({
      next: () => {
        this.refresh();
      },
      error: () => {
        window.alert('No fue posible generar el plan.');
      }
    });
  }

  toggleDetalle(idPlanCamion: number): void {
    this.expandedCamionId = this.expandedCamionId === idPlanCamion ? null : idPlanCamion;
  }

  isExpanded(idPlanCamion: number): boolean {
    return this.expandedCamionId === idPlanCamion;
  }

  getClass(estado: 'OK' | 'WARNING' | 'ERROR'): string {
    switch (estado) {
      case 'OK':
        return 'state-ok';
      case 'WARNING':
        return 'state-warning';
      default:
        return 'state-error';
    }
  }

  trackById(_: number, item: PlanCargaModel): number {
    return item.idPlanCamion;
  }

  trackByTienda(_: number, item: { tiendaId: number }): number {
    return item.tiendaId;
  }
}