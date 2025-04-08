import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlunoService, AlunoCreatePayload } from '../core/services/aluno.service';
import { ApiResponse, Aluno } from '../core/models/aluno.model';
import { finalize } from 'rxjs/operators';
import { NgxMaskDirective } from 'ngx-mask';
import { CURSOS_LISTA } from '../core/constants/cursos.constants'; // Importar a constante da lista de cursos

@Component({
  selector: 'app-cadastro-aluno',
  standalone: true,
  imports: [ CommonModule, FormsModule, RouterLink , NgxMaskDirective],
  templateUrl: './cadastro-aluno.component.html',
  styleUrls: ['./cadastro-aluno.component.css']
})
export class CadastroAlunoComponent implements OnInit, OnDestroy {
  // Propriedades do formulário
  ra: string = '';
  nome: string = '';
  email: string = '';
  telefone: string = '';
  curso: string = '';
  cursosDisponiveis = CURSOS_LISTA;

  mensagemStatus: string = '';
  isLoading: boolean = false;
  private routeSubscription: Subscription | null = null;
  
  isSucessoModalVisible: boolean = false;

    constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alunoService: AlunoService
  ) {}


  ngOnInit(): void {
    this.routeSubscription = this.route.paramMap.subscribe(params => {
      const raFromRoute = params.get('ra');
      if (raFromRoute) {
        this.ra = raFromRoute;
      } else {
        console.error('Nenhum RA fornecido na rota para cadastro.');
        this.router.navigate(['/']);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  cadastrarAluno(): void {
    this.mensagemStatus = ''; // Limpa erros anteriores
    if (!this.nome || !this.email || !this.ra || !this.curso || !this.telefone) {
      this.mensagemStatus = 'Erro: Todos os campos (Nome, E-mail, Telefone, Curso) são obrigatórios.';
      return;
    }

     // 4. Validação específica do E-mail
     const emailLimpo = this.email.trim();
     if (emailLimpo.includes(' ')) {
       this.mensagemStatus = 'Erro: O e-mail não pode conter espaços.';
       return;
     }
     if (!emailLimpo.toLowerCase().endsWith('@puccampinas.edu.br')) {
       this.mensagemStatus = 'Erro: O e-mail deve ser institucional';
       return;
     }
 
     // 5. Validação específica do Telefone (formato básico após máscara)
     // A máscara ajuda muito, mas uma validação extra pode ser útil
     // Ex: Verificar se tem o número esperado de dígitos após remover máscara (opcional)
     const apenasDigitosTelefone = this.telefone.replace(/\D/g, ''); // Remove tudo que não for dígito
     if (apenasDigitosTelefone.length !== 11) { // Verifica se tem 11 dígitos (DDD + 9 dígitos)
        this.mensagemStatus = 'Erro: O formato do telefone parece inválido. Verifique os números.';
        return;
     }

    this.isLoading = true;
    const alunoPayload: AlunoCreatePayload = {
      nome: this.nome.trim(),
      ra: this.ra.trim(),
      email: emailLimpo,
      telefone: this.telefone,
      curso: this.curso
    };

    
    this.alunoService.cadastrarAluno(alunoPayload)
      .pipe(
        finalize(() => this.isLoading = false)
      )
      .subscribe({
        next: (response: ApiResponse<Aluno> | unknown) => {
          console.log('Cadastro - Resposta recebida (HTTP 2xx):', response);

          // ---> SUCESSO: Abrir a Modal de Sucesso <---
          // Removemos a definição de 'mensagemStatus' para sucesso
          this.isSucessoModalVisible = true; // Ativa a nova modal

          // Opcional: Limpar o formulário agora que o cadastro deu certo
          // this.limparFormulario(false); // Passar false para não limpar o RA?

          // Log de diagnóstico (opcional)
          const typedResponse = response as ApiResponse<Aluno>;
          if (typedResponse?.data) {
             console.log(`Aluno ${typedResponse.data.nome} cadastrado com sucesso.`);
          } else {
             console.log(`Aluno com RA ${this.ra} cadastrado com sucesso (corpo da resposta pode variar).`);
          }
        },
        error: (error: Error) => {
          // Em caso de erro na API, ainda usamos mensagemStatus
          console.error('Erro recebido no componente ao cadastrar:', error);
          this.mensagemStatus = `Erro: ${error.message || 'Não foi possível cadastrar o aluno.'}`;
          this.isSucessoModalVisible = false; // Garante que modal de sucesso não apareça
        }
      });
  }

  // ---> NOVOS MÉTODOS PARA MODAL DE SUCESSO <---

  /** Fecha a modal de sucesso. */
  fecharSucessoModal(): void {
    this.isSucessoModalVisible = false;
  }

  /** Fecha a modal e navega para a página inicial. */
  irParaInicio(): void {
    this.fecharSucessoModal();
    this.router.navigate(['/']); // Navega para a rota raiz (Home)
  }

  /** Fecha a modal e navega para a página de empréstimo de notebook. */
  irParaEmprestimo(): void {
    this.fecharSucessoModal();
    // Navega para a página de empréstimo.
    // ATENÇÃO: Esta página atualmente não recebe o RA. Apenas navega.
    this.router.navigate(['/emprestimo-notebook']);
  }

  // Limpa campos do formulário (exceto RA se keepRa for true)
  limparFormulario(keepRa = false): void {
    if (!keepRa) { this.ra = ''; } // Normalmente não limpamos RA vindo da rota
    this.nome = '';
    this.email = '';
    this.telefone = '';
    this.curso = '';
    // Não limpa mensagemStatus aqui, pois pode ser um erro
  }
}