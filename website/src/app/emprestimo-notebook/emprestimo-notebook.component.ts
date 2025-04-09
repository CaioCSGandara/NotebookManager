import { Component, OnInit, OnDestroy } from '@angular/core'; // Adicionar OnDestroy
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotebookService } from '../core/services/notebook.service';
import { Notebook } from '../core/models/notebook.model';
import { finalize } from 'rxjs/operators';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'; // Opcional: para loading
import { ReservaService } from '../core/services/reserva.service'; // <-- Importar ReservaService
import { ReservaPayload } from '../core/models/reserva.model';    // <-- Importar Payload

@Component({
  selector: 'app-emprestimo-notebook',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    // ---> 2. Adicionar Módulos do Material aos imports <---
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule // Opcional
  ],
  templateUrl: './emprestimo-notebook.component.html',
  styleUrls: ['./emprestimo-notebook.component.css']
})
export class EmprestimoNotebookComponent implements OnInit, OnDestroy {
    numeroPatrimonio: string = '';
    mensagemStatus: string = '';
    isLoadingNotebooks: boolean = false;
    erroAoCarregarNotebooks: string | null = null;
    listaNotebooks: Notebook[] = []; // Lista completa da API
    notebooksDisponiveis: Notebook[] = []; // Apenas notebooks com status DISPONIVEL
    notebooksFiltrados: Notebook[] = []; // Lista filtrada baseada na digitação do usuário
    alunoRa: string | null = null;
    private routeSubscription: Subscription | null = null; // Para desinscrever

    isRegistering: boolean = false; // Loading para o botão de registro


    constructor(
      private notebookService: NotebookService,
      private route: ActivatedRoute,
      private reservaService: ReservaService // <-- Injetar ReservaService
    ) {}

    ngOnInit(): void {
      this.routeSubscription = this.route.paramMap.subscribe(params => {
        const raFromRoute = params.get('ra'); // Pega o valor do parâmetro 'ra' da URL
        if (raFromRoute) {
          this.alunoRa = raFromRoute;
          console.log('RA do Aluno recebido:', this.alunoRa);
          // Agora que temos o RA, podemos carregar os notebooks
          this.carregarNotebooks();
        } else {
          console.error('Erro: RA do aluno não encontrado na rota.');
          this.erroAoCarregarNotebooks = 'Não foi possível identificar o aluno (RA ausente). Impossível prosseguir.';
          // Poderia redirecionar ou mostrar erro mais proeminente
        }
      });
    }
    // ngOnDestroy para limpar inscrição
    ngOnDestroy(): void {
      if (this.routeSubscription) {
        this.routeSubscription.unsubscribe();
      }
    }

    carregarNotebooks(): void {
      this.isLoadingNotebooks = true;
      this.erroAoCarregarNotebooks = null;
      this.listaNotebooks = [];
      this.notebooksDisponiveis = []; // Limpa lista de disponíveis
      this.notebooksFiltrados = [];  // Limpa lista filtrada

      this.notebookService.getNotebooks()
        .pipe( finalize(() => this.isLoadingNotebooks = false) )
        .subscribe({
          next: (response) => {
            if (response?.status === 'OK' && Array.isArray(response.data)) {
              this.listaNotebooks = response.data;
              this.notebooksDisponiveis = this.listaNotebooks.filter(
                nb => nb.status === 'DISPONIVEL'
              );
              this.notebooksFiltrados = [...this.notebooksDisponiveis]; // Inicializa filtrados com todos disponíveis
              console.log('Notebooks Disponíveis:', this.notebooksDisponiveis);
            } else {
              this.erroAoCarregarNotebooks = response?.message || 'Não foi possível obter a lista de notebooks (resposta inesperada).';
              console.warn('Resposta inesperada ao carregar notebooks:', response);
            }
          },
          error: (err) => { /* ... tratamento de erro ... */ }
        });
    }

    // ---> 4. NOVO MÉTODO para filtrar sugestões <---
    filtrarNotebooks(): void {
      const valorDigitado = this.numeroPatrimonio ? this.numeroPatrimonio.toLowerCase() : '';

      if (!valorDigitado) {
        // Se campo vazio, mostra todos os disponíveis
        this.notebooksFiltrados = [...this.notebooksDisponiveis];
      } else {
        // Filtra os disponíveis cujo patrimônio começa com o valor digitado
        this.notebooksFiltrados = this.notebooksDisponiveis.filter(notebook =>
          notebook.patrimonio.toLowerCase().includes(valorDigitado) // Ou .startsWith(valorDigitado)
        );
      }
      console.log('Notebooks Filtrados:', this.notebooksFiltrados);
    }

    registrarEmprestimo(): void {
      this.mensagemStatus = ''; // Limpa mensagens
  
      // 1. Validação de RA lido da rota
      if (!this.alunoRa) {
        this.mensagemStatus = 'Erro: Identificação do aluno não encontrada. Não é possível registrar.';
        return;
      }
  
      // 2. Validação de campo Patrimônio vazio
      if (!this.numeroPatrimonio || this.numeroPatrimonio.trim() === '') {
          this.mensagemStatus = 'Erro: Por favor, digite ou selecione o número do patrimônio.';
          return;
      }
      const patrimonioDigitado = this.numeroPatrimonio.trim();
  
      // 3. Validação: Patrimônio está na lista de DISPONÍVEIS?
      const notebookDisponivelEncontrado = this.notebooksDisponiveis.some(
          notebook => notebook.patrimonio === patrimonioDigitado
      );
      if (!notebookDisponivelEncontrado) {
          this.mensagemStatus = `Erro: Patrimônio "${patrimonioDigitado}" não encontrado ou não está disponível para empréstimo.`;
          return;
      }
  
      // 4. Se passou nas validações, montar payload e chamar API
      this.isRegistering = true; // Inicia loading do botão
      const payload: ReservaPayload = {
        alunoRa: this.alunoRa,
        notebookPatrimonio: patrimonioDigitado
      };
  
      console.log('Enviando payload de reserva:', payload);
  
      this.reservaService.criarReserva(payload)
        .pipe(
          finalize(() => this.isRegistering = false) // Finaliza loading do botão
        )
        .subscribe({
          next: (response) => {
            console.log('Resposta da API de reserva:', response);
            // Assume sucesso se chegou aqui (erro seria pego no 'error')
            this.mensagemStatus = `Sucesso: Reserva para RA ${payload.alunoRa} e Patrimônio ${payload.notebookPatrimonio} realizada!`;
            // Limpa o campo e ATUALIZA a lista de notebooks disponíveis
            this.numeroPatrimonio = '';
            this.carregarNotebooks(); // Recarrega a lista para refletir o novo status
          },
          error: (error: Error) => {
            console.error('Erro ao registrar empréstimo:', error);
            this.mensagemStatus = `Erro: ${error.message || 'Não foi possível registrar o empréstimo.'}`;
          }
        });
    }
  }