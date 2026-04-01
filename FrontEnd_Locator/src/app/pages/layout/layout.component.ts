import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

interface MenuItem {
  label: string;
  route: string;
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  readonly isMenuOpen = signal(false);

  readonly menuItems: MenuItem[] = [
    {
      label: 'Reporte rutas',
      route: '/reporte-rutas'
    },
    {
      label: 'Ruta tienda',
      route: '/ruta-tienda'
    },
    {
      label: 'Plan Carga',
      route: '/plan-carga'
    }
  ];

  toggleMenu(): void {
    this.isMenuOpen.update(value => !value);
  }

  closeMenu(): void {
    this.isMenuOpen.set(false);
  }

  trackByRoute(_: number, item: MenuItem): string {
    return item.route;
  }
}