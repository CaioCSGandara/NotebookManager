import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AlunoService } from '../core/services/aluno.service'; // <-- 1. Importar AlunoService
import { finalize } from 'rxjs/operators'; // Importar finalize para parar o loading

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
  isModalVisible: boolean = false; // Modal senha
  enteredPassword: string = '';
  modalErrorMessage: string = ''; // Erro modal senha
  private readonly CORRECT_PASSWORD = 'senha123';
  isCadastroModalVisible: boolean = false; // Modal cadastro
  raParaCadastrar: string = '';

  // --- REMOVER A LISTA SIMULADA ---
  // private validRAs: string[] = ['111222', '333444', '555666', '777888'];

  constructor(
    private router: Router,
    private alunoService: AlunoService // <-- 2. Injetar AlunoService
  ) {}

  // --- Métodos Fluxo RA (Atualizado) ---
  validarRAeNavegar(): void {
    this.loginErrorMessage = '';
    this.modalErrorMessage = '';
    this.isModalVisible = false;
    this.isCadastroModalVisible = false;

    if (!this.ra || this.ra.trim() === '') {
      this.loginErrorMessage = 'Por favor, digite um RA.';
      return;
    }

    this.isLoadingRA = true; // Inicia o loading
    const raNormalizado = this.ra.trim();

    // 3. Chamar o serviço para validar o RA
    this.alunoService.validarRA(raNormalizado)
      .pipe(
        // 5. O finalize garante que o loading termine, ocorrendo sucesso ou erro
        finalize(() => this.isLoadingRA = false)
      )
      .subscribe({
        next: (isValid) => { // Callback para sucesso
          if (isValid) {
            // RA VÁLIDO: Navega para empréstimo notebook
            console.log('RA válido (API):', raNormalizado);
            this.router.navigate(['/emprestimo-notebook']);
          } else {
            // RA INVÁLIDO: Abre modal de confirmação de cadastro
            console.warn('RA inválido (API):', raNormalizado);
            this.raParaCadastrar = raNormalizado;
            this.abrirCadastroModal();
          }
        },
        error: (err) => { // Callback para erro na chamada (já tratado no service, mas pode adicionar mais aqui)
          console.error('Erro na subscrição de validarRA:', err);
          this.loginErrorMessage = 'Erro ao validar RA. Verifique a conexão ou tente mais tarde.';
          // O loading já é parado pelo finalize
        }
        // complete: () => { // Opcional: Executa quando o Observable completa }
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
    this.router.navigate(['/cadastro-usuario', this.raParaCadastrar]);
    this.raParaCadastrar = '';
  }
}