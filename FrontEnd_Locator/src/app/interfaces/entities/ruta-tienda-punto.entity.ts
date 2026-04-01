export interface RutaTiendaPuntoEntity {
  id: number;
  rutaTiendaId: number;
  orden: number;
  estado: string;
  usuarioCreacion: string;
  fechaCreacion: string;
  usuarioActualizacion: string | null;
  fechaActualizacion: string | null;
}