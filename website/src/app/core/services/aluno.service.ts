import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs'; // Importar 'of' para retornar Observable em caso de erro
import { catchError, map } from 'rxjs/operators'; // Importar operadores RxJS
import { ApiResponse, Aluno } from '../models/aluno.model'; // Importar interfaces

@Injectable({
  providedIn: 'root' // Torna o serviço disponível em toda a aplicação
})
export class AlunoService {
  private apiUrl = 'http://localhost:8080'; // Base URL da sua API

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
    // Chama o método para obter todos os alunos
    return this.getAlunos().pipe(
      map(response => {
        // Verifica se a resposta foi OK e se existem dados
        if (response && response.status === 'OK' && response.data) {
          // Procura na lista 'data' se algum aluno tem o RA correspondente
          // Usar trim() para remover espaços extras do RA da API, se necessário
          return response.data.some(aluno => aluno.ra.trim() === ra.trim());
        }
        // Se a resposta não for OK ou não houver dados, considera inválido
        return false;
      }),
      catchError(error => {
        // Se ocorrer um erro na chamada HTTP (ex: API offline), loga e retorna false
        console.error('Erro ao validar RA:', error);
        return of(false); // Retorna um Observable de 'false'
      })
    );
  }

  /**
   * Handler básico de erros para chamadas HTTP.
   * @param operation - Nome da operação que falhou.
   * @param result - Valor opcional para retornar como resultado do Observable.
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: HttpErrorResponse): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`); // Log do erro
      // Retorna um resultado seguro para a aplicação continuar funcionando.
      return of(result as T);
    };
  }

   // --- Futuros métodos ---
   // cadastrarAluno(alunoData: Omit<Aluno, 'id'>): Observable<ApiResponse<Aluno>> { ... }
}