// src/app/core/services/reserva.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ReservaPayload, ReservaCreateResponse } from '../models/reserva.model'; // Ajuste o caminho
import { ApiResponse } from '../models/api-response.model'; // Ajuste o caminho

@Injectable({
  providedIn: 'root'
})
export class ReservaService {
  private apiUrl = 'http://localhost:8080';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  /**
   * Cria uma nova reserva (empréstimo) de notebook.
   * @param payload Dados da reserva (RA do aluno e Patrimônio do notebook).
   * @returns Observable com a resposta da API.
   */
  criarReserva(payload: ReservaPayload): Observable<ReservaCreateResponse> {
    return this.http.post<ReservaCreateResponse>(`${this.apiUrl}/reservas`, payload, this.httpOptions)
      .pipe(
        catchError(this.handleReservaError) // Tratamento de erro específico
      );
  }

  private handleReservaError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro desconhecido ao tentar criar a reserva.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro de rede/cliente: ${error.error.message}`;
    } else {
      // Tenta pegar mensagens específicas do backend
      if (error.status === 400) { // Bad Request (Ex: RA inválido, Patrimônio inválido/não disponível)
        errorMessage = `Erro [${error.status}]: ${error.error?.message || 'Dados inválidos para reserva.'}`;
      } else if (error.status === 409) { // Conflict (Ex: Aluno já tem empréstimo, Notebook já emprestado)
        errorMessage = `Conflito [${error.status}]: ${error.error?.message || 'Não foi possível realizar a reserva (verifique regras).'}`;
      } else {
        errorMessage = `Erro do servidor [${error.status}]: ${error.error?.message || 'Tente novamente mais tarde.'}`;
      }
    }
    console.error('Erro ao criar reserva:', error);
    return throwError(() => new Error(errorMessage));
  }

}