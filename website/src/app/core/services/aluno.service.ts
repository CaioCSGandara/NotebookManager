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
  /*
  * Busca os dados de um aluno específico pelo RA.
  * Trata o erro 404 Not Found retornando uma estrutura ApiResponse específica.
  * @param ra O RA do aluno a ser buscado.
  * @returns Observable com a resposta da API (Aluno encontrado, Não encontrado ou Erro).
  */
 getAlunoPorRa(ra: string): Observable<ApiResponse<Aluno | null>> {
   return this.http.get<ApiResponse<Aluno>>(`${this.apiUrl}/alunos/ra/${ra}`)
     .pipe(
       catchError((error: HttpErrorResponse) => {
         if (error.status === 404) {
           // Se API retornou 404, transforma em uma resposta 'normal' com status NOT_FOUND
           console.log(`Aluno com RA ${ra} não encontrado (404).`);
           return of({ status: 'NOT_FOUND', data: null, message: 'Aluno não encontrado.' });
         } else {
           // Para outros erros (500, rede, etc.), loga e retorna uma estrutura de erro genérica
           console.error(`Erro ao buscar aluno por RA ${ra}: ${error.message}`);
           return of({ status: 'ERROR', data: null, message: 'Erro ao conectar com o servidor para buscar aluno.' });
           // Alternativa: Poderia usar throwError aqui se quisesse tratar no 'error' do subscribe no componente
           // return throwError(() => new Error('Erro ao conectar com o servidor para buscar aluno.'));
         }
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