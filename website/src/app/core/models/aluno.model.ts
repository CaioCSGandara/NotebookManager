// Interface para representar um objeto Aluno como vem da API
export interface Aluno {
  id: number;
  nome: string;
  ra: string;
  email: string;
  telefone: string;
  curso: string;
}

export interface ApiResponse<T> {
  status: string;
  data: T;
  message: string | null;
} 