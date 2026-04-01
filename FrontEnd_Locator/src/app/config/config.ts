import { environment } from "../environments/environment";

export const APP_CONFIG = {
  api:{
    rutasGeo: `${environment.apiUrl}/rutas-geo`,
    rutaTienda: `${environment.apiUrl}/ruta-tienda`,
    rutaTiendaPunto: `${environment.apiUrl}/ruta-tienda-punto`,
    planCarga: `${environment.apiUrl}/plan-carga`
  }
}