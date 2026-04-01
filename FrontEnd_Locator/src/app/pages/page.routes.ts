import { Routes } from "@angular/router";
import { PlanCargaComponent } from "./plan-carga/plan-carga.component";
import { ReporteRutasComponent } from './reporte-rutas/reporte-rutas.component';
import { RutaTiendaComponent } from './ruta-tienda/ruta-tienda.component';

export const pageRoutes: Routes = [
    {
        path: '',
        redirectTo: 'reporte-rutas',
        pathMatch: 'full'
    },
    {
        path: 'reporte-rutas',
        component: ReporteRutasComponent
    },
    {
        path: 'ruta-tienda',
        component: RutaTiendaComponent
    },
    {
        path: 'plan-carga',
        component: PlanCargaComponent
    }
];