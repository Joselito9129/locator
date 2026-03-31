import { Routes } from "@angular/router";
import { ReporteRutasComponent } from './reporte-rutas/reporte-rutas.component';

export const pageRoutes: Routes = [
    {
        path: '',
        redirectTo: 'reporte-rutas',
        pathMatch: 'full'
    },
    {
        path: 'reporte-rutas',
        component: ReporteRutasComponent
    }
];