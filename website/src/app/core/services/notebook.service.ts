import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
// Ajuste o caminho se seus models estiverem em local diferente
import { NotebookListResponse, Notebook } from '../models/notebook.model';

@Injectable({
  providedIn: 'root'
})
export class NotebookService {
  private apiUrl = 'http://localhost:8080'; // Base URL da sua API

  constructor(private http: HttpClient) {}

  /**
   * Busca a lista completa de notebooks da API.
   */
  getNotebooks(): Observable<NotebookListResponse> {
    return this.http.get<NotebookListResponse>(`${this.apiUrl}/notebooks?sort=patrimonio,asc`)
      .pipe(
        catchError(this.handleError<NotebookListResponse>('getNotebooks', { status: 'ERROR', data: [], message: 'Falha ao buscar notebooks.' }))
      );
  }

  /**
   * Handler básico de erros para chamadas HTTP.
   * (Pode ser movido para um serviço base de API se ficar repetitivo)
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: HttpErrorResponse): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`); // Log do erro
      // Retorna um resultado seguro para a aplicação continuar.
      return of(result as T);
    };
  }

  // Futuros métodos: getNotebookPorPatrimonio, registrarEmprestimo, etc.
}