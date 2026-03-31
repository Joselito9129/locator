export interface RutaGeoEntity {
  id: number;
  latitudOrigen: number;
  longitudOrigen: number;
  latitudDestino: number;
  longitudDestino: number;
  estado: string;
  usuarioCreacion: string;
  fechaCreacion: string;
  guia: string;
  data?: unknown;
  dataList?: unknown[] | null;
  message?: string | null;
  success?: boolean;
}