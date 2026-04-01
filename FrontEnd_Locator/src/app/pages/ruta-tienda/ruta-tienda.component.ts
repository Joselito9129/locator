import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BehaviorSubject, catchError, map, of, shareReplay, startWith, switchMap, take } from 'rxjs';

import { RutaTiendaPuntoRequest } from '../../interfaces/request/ruta-tienda-punto.request';
import { RutaTiendaRequest } from '../../interfaces/request/ruta-tienda.request';
import { RutaTiendaPuntoModel } from '../../models/ruta-tienda-punto.model';
import { RutaTiendaModel } from '../../models/ruta-tienda.model';
import { RutaTiendaService } from '../../service/services/ruta-tienda.service';

interface DataState<T> {
  data: T;
  loading: boolean;
  errorMessage: string | null;
}

@Component({
  selector: 'app-ruta-tienda',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './ruta-tienda.component.html',
  styleUrl: './ruta-tienda.component.scss'
})
export class RutaTiendaComponent {
  private readonly fb = inject(FormBuilder);
  private readonly rutaTiendaService = inject(RutaTiendaService);

  private readonly refreshRutas$ = new BehaviorSubject<void>(undefined);
  private readonly selectedRutaId$ = new BehaviorSubject<number | null>(null);
  private readonly refreshPuntos$ = new BehaviorSubject<void>(undefined);

  selectedRutaIdValue: number | null = null;
  editingRutaId: number | null = null;
  editingPuntoId: number | null = null;

  readonly routeForm = this.fb.nonNullable.group({
    tiendaId: [0, [Validators.required, Validators.min(1)]],
    estado: ['ACTIVO', [Validators.required]],
    usuarioAccion: ['', [Validators.required]]
  });

  readonly pointForm = this.fb.nonNullable.group({
    orden: [1, [Validators.required, Validators.min(1)]],
    estado: ['ACTIVO', [Validators.required]],
    usuarioAccion: ['', [Validators.required]]
  });

  readonly rutasState$ = this.refreshRutas$.pipe(
    switchMap(() =>
      this.rutaTiendaService.getRutas().pipe(
        map((data): DataState<RutaTiendaModel[]> => ({
          data,
          loading: false,
          errorMessage: null
        })),
        startWith({
          data: [],
          loading: true,
          errorMessage: null
        } as DataState<RutaTiendaModel[]>),
        catchError(() =>
          of({
            data: [],
            loading: false,
            errorMessage: 'No fue posible cargar las rutas.'
          } as DataState<RutaTiendaModel[]>)
        )
      )
    ),
    shareReplay(1)
  );

  readonly puntosState$ = this.selectedRutaId$.pipe(
    switchMap((rutaId) => {
      if (!rutaId) {
        return of({
          data: [],
          loading: false,
          errorMessage: null
        } as DataState<RutaTiendaPuntoModel[]>);
      }

      return this.refreshPuntos$.pipe(
        switchMap(() =>
          this.rutaTiendaService.getPuntosByRutaId(rutaId).pipe(
            map((data): DataState<RutaTiendaPuntoModel[]> => ({
              data,
              loading: false,
              errorMessage: null
            })),
            startWith({
              data: [],
              loading: true,
              errorMessage: null
            } as DataState<RutaTiendaPuntoModel[]>),
            catchError(() =>
              of({
                data: [],
                loading: false,
                errorMessage: 'No fue posible cargar los puntos de la ruta.'
              } as DataState<RutaTiendaPuntoModel[]>)
            )
          )
        )
      );
    }),
    shareReplay(1)
  );

  loadData(): void {
    this.refresh();
  }

  refresh(): void {
    this.refreshRutas$.next();

    if (this.selectedRutaIdValue) {
      this.refreshPuntos$.next();
    }
  }

  selectRuta(ruta: RutaTiendaModel): void {
    this.selectedRutaIdValue = ruta.id;
    this.selectedRutaId$.next(ruta.id);
    this.editingPuntoId = null;
    this.resetPointForm();
  }

  editRuta(ruta: RutaTiendaModel): void {
    this.editingRutaId = ruta.id;
    this.routeForm.patchValue({
      tiendaId: ruta.tiendaId,
      estado: ruta.estado,
      usuarioAccion: ''
    });
    this.selectRuta(ruta);
  }

  resetRutaForm(): void {
    this.editingRutaId = null;
    this.routeForm.reset({
      tiendaId: 0,
      estado: 'ACTIVO',
      usuarioAccion: ''
    });
  }

  saveRuta(): void {
    this.routeForm.markAllAsTouched();

    if (this.routeForm.invalid) {
      return;
    }

    const { tiendaId, estado, usuarioAccion } = this.routeForm.getRawValue();

    if (this.editingRutaId) {
      const request: RutaTiendaRequest = {
        tiendaId,
        estado,
        usuarioActualizacion: usuarioAccion
      };

      this.rutaTiendaService.updateRuta(this.editingRutaId, request).pipe(take(1)).subscribe({
        next: (ruta) => {
          this.selectedRutaIdValue = ruta.id;
          this.selectedRutaId$.next(ruta.id);
          this.refreshRutas$.next();
          this.refreshPuntos$.next();
          this.resetRutaForm();
        },
        error: () => {
          window.alert('No fue posible actualizar la ruta.');
        }
      });

      return;
    }

    const request: RutaTiendaRequest = {
      tiendaId,
      estado,
      usuarioCreacion: usuarioAccion
    };

    this.rutaTiendaService.createRuta(request).pipe(take(1)).subscribe({
      next: (ruta) => {
        this.selectedRutaIdValue = ruta.id;
        this.selectedRutaId$.next(ruta.id);
        this.refreshRutas$.next();
        this.refreshPuntos$.next();
        this.resetRutaForm();
      },
      error: () => {
        window.alert('No fue posible crear la ruta.');
      }
    });
  }

  deleteRuta(ruta: RutaTiendaModel): void {
    const confirmed = window.confirm(`¿Deseas eliminar la ruta ${ruta.id}?`);

    if (!confirmed) {
      return;
    }

    this.rutaTiendaService.deleteRuta(ruta.id).pipe(take(1)).subscribe({
      next: () => {
        if (this.selectedRutaIdValue === ruta.id) {
          this.selectedRutaIdValue = null;
          this.selectedRutaId$.next(null);
          this.editingPuntoId = null;
          this.resetPointForm();
        }

        if (this.editingRutaId === ruta.id) {
          this.resetRutaForm();
        }

        this.refreshRutas$.next();
      },
      error: () => {
        window.alert('No fue posible eliminar la ruta. Verifica si tiene puntos asociados.');
      }
    });
  }

  editPunto(punto: RutaTiendaPuntoModel): void {
    this.editingPuntoId = punto.id;
    this.pointForm.patchValue({
      orden: punto.orden,
      estado: punto.estado,
      usuarioAccion: ''
    });
  }

  resetPointForm(): void {
    this.editingPuntoId = null;
    this.pointForm.reset({
      orden: 1,
      estado: 'ACTIVO',
      usuarioAccion: ''
    });
  }

  savePunto(): void {
    this.pointForm.markAllAsTouched();

    if (this.pointForm.invalid || !this.selectedRutaIdValue) {
      return;
    }

    const { orden, estado, usuarioAccion } = this.pointForm.getRawValue();

    if (this.editingPuntoId) {
      const request: RutaTiendaPuntoRequest = {
        rutaTiendaId: this.selectedRutaIdValue,
        orden,
        estado,
        usuarioActualizacion: usuarioAccion
      };

      this.rutaTiendaService.updatePunto(this.editingPuntoId, request).pipe(take(1)).subscribe({
        next: () => {
          this.refreshPuntos$.next();
          this.resetPointForm();
        },
        error: () => {
          window.alert('No fue posible actualizar el punto.');
        }
      });

      return;
    }

    const request: RutaTiendaPuntoRequest = {
      rutaTiendaId: this.selectedRutaIdValue,
      orden,
      estado,
      usuarioCreacion: usuarioAccion
    };

    this.rutaTiendaService.createPunto(request).pipe(take(1)).subscribe({
      next: () => {
        this.refreshPuntos$.next();
        this.resetPointForm();
      },
      error: () => {
        window.alert('No fue posible crear el punto.');
      }
    });
  }

  deletePunto(punto: RutaTiendaPuntoModel): void {
    const confirmed = window.confirm(`¿Deseas eliminar el punto ${punto.id}?`);

    if (!confirmed) {
      return;
    }

    this.rutaTiendaService.deletePunto(punto.id).pipe(take(1)).subscribe({
      next: () => {
        if (this.editingPuntoId === punto.id) {
          this.resetPointForm();
        }

        this.refreshPuntos$.next();
      },
      error: () => {
        window.alert('No fue posible eliminar el punto.');
      }
    });
  }

  trackById(_: number, item: { id: number }): number {
    return item.id;
  }

  isInvalid(
    controlName: 'tiendaId' | 'estado' | 'usuarioAccion',
    formType: 'ruta' | 'punto'
  ): boolean {
    const form = formType === 'ruta' ? this.routeForm : this.pointForm;
    const control = form.controls[controlName as keyof typeof form.controls];

    return !!control && control.invalid && (control.dirty || control.touched);
  }

  isPointInvalid(controlName: 'orden' | 'estado' | 'usuarioAccion'): boolean {
    const control = this.pointForm.controls[controlName];
    return control.invalid && (control.dirty || control.touched);
  }
}