import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AlunoService } from '../core/services/aluno.service';
import { Aluno } from '../core/models/aluno.model';
import { finalize } from 'rxjs/operators';
import { CURSOS_MAP } from '../core/constants/cursos.constants'; 

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ CommonModule, FormsModule ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  // --- Propriedades ---
  ra: string = '';
  loginErrorMessage: string = '';
  isLoadingRA: boolean = false;
  // Modal Senha
  isModalVisible: boolean = false;
  enteredPassword: string = '';
  modalErrorMessage: string = '';
  private readonly CORRECT_PASSWORD = 'senha123';
  // Modal Cadastro
  isCadastroModalVisible: boolean = false;
  raParaCadastrar: string = '';
  // Modal Confirmação Aluno
  isConfirmacaoAlunoModalVisible: boolean = false;
  alunoConfirmado: Aluno | null = null;

  constructor(
    private router: Router,
    private alunoService: AlunoService
  ) {}

  // --- Getters para Dados Mascarados/Formatados ---

  get nomeMascarado(): string {
    if (!this.alunoConfirmado?.nome) {
      return '';
    }
    const partesNome = this.alunoConfirmado.nome.split(' ');
    if (partesNome.length <= 1) {
      return this.alunoConfirmado.nome; // Retorna nome completo se não houver sobrenome
    }
    // Retorna o primeiro nome + asteriscos
    return `${partesNome[0]} ****`;
  }

  get emailMascarado(): string {
    if (!this.alunoConfirmado?.email) {
      return '';
    }
    const emailCompleto = this.alunoConfirmado.email;
    const partesEmail = emailCompleto.split('@');
    if (partesEmail.length !== 2 || partesEmail[1].toLowerCase() !== 'puccampinas.edu.br') {
      return 'E-mail inválido'; // Ou retorna o email original se não for do domínio esperado
    }

    const localPart = partesEmail[0];
    const domain = partesEmail[1];
    const dotIndex = localPart.indexOf('.');

    if (dotIndex === -1) {
      // Sem ponto na parte local, retorna como está (ou aplique outra regra se desejar)
      return emailCompleto;
      // Alternativa: mascarar tudo após N caracteres:
      // return localPart.substring(0, 3) + '****@' + domain;
    } else {
      // Mascara tudo após o primeiro ponto na parte local
      const parteVisivel = localPart.substring(0, dotIndex + 1); // Inclui o ponto
      return `${parteVisivel}****@${domain}`;
    }
  }

  get telefoneMascarado(): string {
    if (!this.alunoConfirmado?.telefone) {
      return ''; // Ou 'Não informado'
    }
    // Assume formato (XX)XXXXX-XXXX ou similar vindo da API
    const telefone = this.alunoConfirmado.telefone;
    // Remove não-dígitos para garantir contagem correta, caso a API envie sem máscara
    const digitos = telefone.replace(/\D/g, '');
    if (digitos.length < 11) return telefone; // Retorna original se não tiver dígitos suficientes

    // Reconstrói a máscara padrão e mascara os últimos 4 dígitos
    // Ex: (19)98765-4321 -> (19)98765-****
    // Pega os 7 primeiros dígitos (DDD + 5 iniciais)
    const parteVisivelNumeros = digitos.substring(0, 7);
    // Formata essa parte inicial (ajuste se o formato for diferente)
    const ddd = parteVisivelNumeros.substring(0, 2);
    const inicioNumero = parteVisivelNumeros.substring(2);
    return `(${ddd})${inicioNumero}-****`;

    // Alternativa mais simples se a API *sempre* envia no formato (XX)XXXXX-XXXX:
    // if (telefone.length >= 14) { // Verifica tamanho mínimo da string formatada
    //   return telefone.substring(0, telefone.length - 4) + '****';
    // }
    // return telefone; // Retorna como está se não tiver o formato esperado
  }

  get cursoDisplay(): string {
    if (!this.alunoConfirmado?.curso) {
      return '';
    }
    // Usa o mapa importado. Se não encontrar a chave, retorna o valor original.
    return CURSOS_MAP[this.alunoConfirmado.curso] || this.alunoConfirmado.curso;
  }

  // --- Métodos Fluxo RA (VALIDAÇÃO ADICIONADA) ---
  validarRAeNavegar(): void {
    // Limpa mensagens e estados anteriores
    this.loginErrorMessage = '';
    this.modalErrorMessage = '';
    this.isModalVisible = false;
    this.isCadastroModalVisible = false;
    this.isConfirmacaoAlunoModalVisible = false; // Garante que a nova modal esteja fechada
    this.alunoConfirmado = null; // Limpa dados de aluno anterior

    // Validação de campo vazio
    if (!this.ra || this.ra.trim() === '') {
      this.loginErrorMessage = 'Por favor, digite um RA.';
      return;
    }
    const raLimpo = this.ra.trim();

    // Validação de formato (8 dígitos)
    const raValidoRegex = /^\d{8}$/;
    if (!raValidoRegex.test(raLimpo)) {
      this.loginErrorMessage = 'RA inválido. Deve conter exatamente 8 números.';
      return;
    }

    // Se passou nas validações, chama a nova API
    this.isLoadingRA = true;
    this.alunoService.getAlunoPorRa(raLimpo) // Chama o novo método do serviço
      .pipe(
        finalize(() => this.isLoadingRA = false)
      )
      .subscribe({
        next: (response) => {
          console.log('Resposta da API getAlunoPorRa:', response); // Log para depuração

          // Verifica o STATUS dentro da resposta da API
          switch (response.status) {
            case 'OK':
              if (response.data) {
                // ALUNO ENCONTRADO: Guarda os dados e abre modal de confirmação
                this.alunoConfirmado = response.data;
                this.abrirConfirmacaoAlunoModal();
              } else {
                // Status OK, mas sem dados? Tratar como erro inesperado
                this.loginErrorMessage = 'Erro inesperado ao buscar dados do aluno.';
              }
              break;
            case 'NOT_FOUND':
              // ALUNO NÃO ENCONTRADO: Abre modal para cadastro
              this.raParaCadastrar = raLimpo;
              this.abrirCadastroModal();
              break;
            default:
              // Outros status ('ERROR' ou inesperados)
              this.loginErrorMessage = response.message || 'Não foi possível verificar o RA.';
          }
        },
        error: (err) => {
          // Erro na comunicação HTTP (se o catchError no serviço usasse throwError)
          console.error('[HomeComponent] Erro na subscrição de getAlunoPorRa:', err);
          this.loginErrorMessage = 'Erro de comunicação ao verificar RA.';
        }
      });
  }

  // --- Métodos Modal Senha (Empréstimos Ativos) ---
  abrirModalSenha(): void { /* ... sem alterações ... */
    this.loginErrorMessage = '';
    this.isCadastroModalVisible = false;
    this.enteredPassword = '';
    this.modalErrorMessage = '';
    this.isModalVisible = true;
  }
  fecharModalSenha(): void { /* ... sem alterações ... */ this.isModalVisible = false; }
  verificarSenhaEEntrarEmprestimosAtivos(): void { /* ... sem alterações ... */
    if (this.enteredPassword === this.CORRECT_PASSWORD) {
      this.fecharModalSenha();
      this.router.navigate(['/emprestimos-ativos']);
    } else {
      this.modalErrorMessage = 'Senha incorreta. Tente novamente.';
      this.enteredPassword = '';
    }
  }

  // --- Métodos Modal Confirmação de Cadastro ---
  abrirCadastroModal(): void { /* ... sem alterações ... */
    this.isModalVisible = false;
    this.isCadastroModalVisible = true;
  }
  cancelarCadastro(): void { /* ... sem alterações ... */
    this.isCadastroModalVisible = false;
    this.raParaCadastrar = '';
  }
  confirmarCadastro(): void { /* ... sem alterações ... */
    this.isCadastroModalVisible = false;
    this.router.navigate(['/cadastro-aluno', this.raParaCadastrar]);
    this.raParaCadastrar = '';
  }
  // ---> NOVOS MÉTODOS: Modal Confirmação Aluno (RA encontrado) <---
  abrirConfirmacaoAlunoModal(): void {
    this.isConfirmacaoAlunoModalVisible = true;
  }

  fecharConfirmacaoAlunoModal(): void {
    this.isConfirmacaoAlunoModalVisible = false;
    this.alunoConfirmado = null; // Limpa os dados ao fechar
  }

  confirmarAlunoENavegar(): void {
    if (this.alunoConfirmado?.ra) { // Garante que temos o RA
       const raDoAluno = this.alunoConfirmado.ra;
       this.fecharConfirmacaoAlunoModal();
       // ---> Passa o RA na navegação <---
       this.router.navigate(['/emprestimo-notebook', raDoAluno]);
    } else {
       console.error("Erro: Não foi possível obter o RA do aluno confirmado para navegar.");
       this.fecharConfirmacaoAlunoModal();
       // Talvez mostrar uma mensagem de erro aqui
    }
  }
}