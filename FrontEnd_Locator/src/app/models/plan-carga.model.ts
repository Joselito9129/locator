export interface PlanCargaTiendaModel {
  tiendaId: number;
  pesoAsignado: number;
  volumenAsignado: number | null;
}

export interface PlanCargaModel {
  idPlanCamion: number;
  planId: number;
  tipoCamion: string;
  capacidadPeso: number;
  capacidadVolumen: number | null;
  pesoActual: number;
  volumenActual: number | null;
  porcentajeOcupacion: number;
  estado: string;
  estadoVisual: 'OK' | 'WARNING' | 'ERROR';
  tiendas: PlanCargaTiendaModel[];
}