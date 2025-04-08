// src/app/core/services/aluno.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http'; // Importar HttpHeaders se necessário
import { Observable, of, throwError } from 'rxjs'; // Importar throwError
import { catchError, map } from 'rxjs/operators';
import { ApiResponse, Aluno } from '../models/aluno.model';

// Tipo para os dados enviados no cadastro (Aluno sem o 'id')
export type AlunoCreatePayload = Omit<Aluno, 'id'>;

@Injectable({
  providedIn: 'root'
})
export class AlunoService {
  private apiUrl = 'http://localhost:8080';

  // Opcional: Definir cabeçalhos padrão, como Content-Type
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}

  /**
   * Busca todos os alunos da API.
   * @returns Observable com a resposta da API contendo a lista de alunos.
   */
  getAlunos(): Observable<ApiResponse<Aluno[]>> {
    return this.http.get<ApiResponse<Aluno[]>>(`${this.apiUrl}/alunos`)
      .pipe(
        catchError(this.handleError<ApiResponse<Aluno[]>>('getAlunos', { status: 'ERROR', data: [], message: 'Falha ao buscar alunos.' }))
      );
  }

  /**
   * Valida se um RA existe na lista de alunos obtida da API.
   * @param ra - O RA a ser validado.
   * @returns Observable<boolean> - true se o RA existe, false caso contrário ou se ocorrer erro.
   */
  validarRA(ra: string): Observable<boolean> {
    const raParaValidar = ra.trim(); // Limpa espaços do RA de entrada

    // Chama o método para obter todos os alunos
    return this.getAlunos().pipe(
      map(response => {
        // Verifica se a resposta foi OK e se existem dados (e se data é um array)
        if (response && response.status === 'OK' && Array.isArray(response.data)) {
          // Usa .some() para verificar se algum aluno no array 'data' tem o RA correspondente
          // Garante que aluno.ra existe, converte para string, remove espaços e compara
          const encontrado = response.data.some(aluno =>
             aluno.ra && aluno.ra.toString().trim() === raParaValidar
          );
          return encontrado; // Retorna true se encontrou, false caso contrário
        }
        // Se a resposta não for OK ou não houver dados válidos, considera inválido
        return false;
      }),
      catchError(error => {
        // Se ocorrer um erro na chamada HTTP, loga e retorna false
        // Mantemos este log pois é útil para erros reais da API
        console.error('[AlunoService] Erro ao validar RA via API:', error);
        return of(false); // Retorna um Observable de 'false'
      })
    );
  }

  cadastrarAluno(alunoData: AlunoCreatePayload): Observable<ApiResponse<Aluno>> {
    return this.http.post<ApiResponse<Aluno>>(`${this.apiUrl}/alunos`, alunoData, this.httpOptions)
      .pipe(
        catchError(this.handleCadastroError) // Tratamento de erro específico para POST
      );
  }
  

  /**
   * Handler básico de erros para chamadas HTTP.
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: HttpErrorResponse): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`); // Log do erro real
      return of(result as T);
    };
  }

  // Handler de erro específico para POST, pode retornar mais detalhes do erro
  private handleCadastroError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro desconhecido ao tentar cadastrar.';
    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente ou de rede.
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // O backend retornou um código de erro.
      // O corpo da resposta pode conter pistas sobre o que deu errado.
      // Tenta extrair uma mensagem mais específica do backend, se houver.
      if (error.status === 400) { // Exemplo: Bad Request (dados inválidos)
        errorMessage = `Erro de validação [${error.status}]: ${error.error?.message || 'Verifique os dados informados.'}`;
      } else if (error.status === 409) { // Exemplo: Conflict (RA/Email já existe)
         errorMessage = `Conflito [${error.status}]: ${error.error?.message || 'RA ou E-mail já cadastrado.'}`;
      } else {
        errorMessage = `Erro do servidor [${error.status}]: ${error.error?.message || 'Tente novamente mais tarde.'}`;
      }
    }
    console.error('Erro no cadastro:', error);
    // Retorna um observable com um erro user-facing.
    // Usar 'throwError' passa o erro para o .subscribe() no componente.
    return throwError(() => new Error(errorMessage));
  }
}