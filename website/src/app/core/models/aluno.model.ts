// Interface para representar um objeto Aluno como vem da API
export interface Aluno {
    id: number;
    nome: string;
    ra: string;
    email: string;
    telefone?: string | null; // Marcar como opcional se puder ser nulo/ausente
    curso: string;
  }
  
  // Interface genérica para a resposta padrão da sua API
  export interface ApiResponse<T> {
    status: string;       // Ex: "OK", "ERROR"
    data: T;              // Os dados reais (pode ser Aluno[], Aluno, etc.)
    message: string | null; // Mensagem opcional
  }