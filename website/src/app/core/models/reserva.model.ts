import { ApiResponse } from './api-response.model';

// Payload para criar uma reserva
export interface ReservaPayload {
  alunoRa: string;
  notebookPatrimonio: string;
}

// Exemplo de como poderia ser uma Reserva retornada pela API
export interface Reserva {
    id: number;
    alunoRa: string;
    notebookPatrimonio: string;
    inicioEm: string;
    terminoEm?: string | null;
}

// Resposta esperada da API ao criar reserva - AGORA USA ApiResponse IMPORTADO
export type ReservaCreateResponse = ApiResponse<Reserva | null>;