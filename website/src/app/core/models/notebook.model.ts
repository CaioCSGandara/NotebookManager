import { ApiResponse } from './api-response.model';

// Definir os possíveis status de um notebook (ajuste se houver outros)
export type NotebookStatus = "DISPONIVEL" | "EMPRESTADO" | "AFASTADO";

// Interface para representar um Notebook
export interface Notebook {
  id: number;
  patrimonio: string;
  status: NotebookStatus;
}

// Tipo específico para a resposta da lista de notebooks
export type NotebookListResponse = ApiResponse<Notebook[]>;