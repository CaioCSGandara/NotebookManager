// src/app/core/services/aluno.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiResponse, Aluno } from '../models/aluno.model';

@Injectable({
  providedIn: 'root'
})
export class AlunoService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAlunos(): Observable<ApiResponse<Aluno[]>> {
    // ... (código existente sem alterações) ...
    return this.http.get<ApiResponse<Aluno[]>>(`${this.apiUrl}/alunos`)
        .pipe(
          catchError(this.handleError<ApiResponse<Aluno[]>>('getAlunos', { status: 'ERROR', data: [], message: 'Falha ao buscar alunos.' }))
        );
  }

  validarRA(ra: string): Observable<boolean> {
    const raParaValidar = ra.trim(); // Garante que o RA de entrada está sem espaços extras
    console.log(`[AlunoService] Iniciando validação para RA: "${raParaValidar}"`); // Log 1: RA a validar

    return this.getAlunos().pipe(
      map(response => {
        console.log('[AlunoService] Resposta da API recebida:', response); // Log 2: Resposta completa

        if (response && response.status === 'OK' && Array.isArray(response.data)) {
          console.log(`[AlunoService] Número de alunos na resposta: ${response.data.length}`); // Log 3: Quantos alunos vieram

          // Log detalhado da comparação
          let encontrado = false;
          response.data.forEach((aluno, index) => {
            const raDaApi = aluno.ra ? aluno.ra.toString().trim() : ''; // Converte para string e trim
            const comparacao = raDaApi === raParaValidar;
             console.log(`[AlunoService] Comparando[${index}]: API RA="${raDaApi}" (tipo: ${typeof raDaApi}) === Input RA="${raParaValidar}" (tipo: ${typeof raParaValidar}) -> ${comparacao}`); // Log 4: Detalhe da comparação
            if (comparacao) {
              encontrado = true;
            }
          });

          // A lógica original com .some() é mais eficiente, mas vamos manter o log detalhado por enquanto
          // const encontrado = response.data.some(aluno => {
          //   const raDaApi = aluno.ra ? aluno.ra.toString().trim() : '';
          //   const comparacao = raDaApi === raParaValidar;
          //   console.log(`[AlunoService] Comparando: API RA="${raDaApi}" === Input RA="${raParaValidar}" -> ${comparacao}`); // Log dentro do some
          //   return comparacao;
          // });

          console.log(`[AlunoService] RA "${raParaValidar}" foi encontrado? ${encontrado}`); // Log 5: Resultado da busca
          return encontrado; // Retorna o resultado da busca
        } else {
          console.warn('[AlunoService] Resposta da API inválida ou sem dados.'); // Log 6: Problema na resposta
          return false; // Retorna false se a resposta não for válida
        }
      }),
      catchError(error => {
        console.error('[AlunoService] Erro no pipe de validarRA:', error); // Log 7: Erro no pipe
        return of(false);
      })
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    // ... (código existente sem alterações) ...
     return (error: HttpErrorResponse): Observable<T> => {
        console.error(`${operation} failed: ${error.message}`);
        return of(result as T);
      };
  }
}