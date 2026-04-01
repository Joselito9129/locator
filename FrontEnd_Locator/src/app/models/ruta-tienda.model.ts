export interface RutaTiendaModel {
  id: number;
  tiendaId: number;
  estado: string;
  usuarioCreacion: string;
  fechaCreacion: string;
  usuarioActualizacion: string | null;
  fechaActualizacion: string | null;
  fechaCreacionLabel: string;
  fechaActualizacionLabel: string;
}