export interface PlanCargaTiendaResponse {
  tiendaId: number;
  pesoAsignado: number;
  volumenAsignado: number | null;
}

export interface PlanCargaResponse {
  idPlanCamion: number;
  planId: number;
  tipoCamion: string;
  capacidadPeso: number;
  capacidadVolumen: number | null;
  pesoActual: number;
  volumenActual: number | null;
  porcentajeOcupacion: number;
  estado: string;
  tiendas: PlanCargaTiendaResponse[];
}