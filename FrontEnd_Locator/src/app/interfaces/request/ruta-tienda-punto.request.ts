export interface RutaTiendaPuntoRequest {
  rutaTiendaId: number;
  orden: number;
  estado: string;
  usuarioCreacion?: string;
  usuarioActualizacion?: string;
}