import { Routes } from '@angular/router';
import { LayoutComponent } from './pages/layout/layout.component';
import { pageRoutes } from './pages/page.routes';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: pageRoutes
  },
  {
    path: '**',
    redirectTo: ''
  }
];