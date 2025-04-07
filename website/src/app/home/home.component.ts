// src/app/home/home.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ CommonModule, FormsModule ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  // --- Fluxo de Registro de Empréstimo (via RA) ---
  ra: string = '';
  loginErrorMessage: string = ''; // Erro se o campo RA estiver vazio
  isLoadingRA: boolean = false;
  private validRAs: string[] = ['111222', '333444', '555666', '777888']; // Simulação BD

  // --- Fluxo de Ver Empréstimos Ativos (via Modal/Senha) ---
  isModalVisible: boolean = false;
  enteredPassword: string = '';
  modalErrorMessage: string = '';
  private readonly CORRECT_PASSWORD = 'senha123';

  // --- NOVO: Fluxo Modal Confirmação de Cadastro ---
  isCadastroModalVisible: boolean = false;
  raParaCadastrar: string = ''; // Armazena o RA inválido para possível cadastro

  constructor(private router: Router) {}

  // --- Métodos Fluxo RA ---
  validarRAeNavegar(): void {
    this.loginErrorMessage = ''; // Limpa erro de campo vazio
    this.modalErrorMessage = ''; // Limpa erro da outra modal também
    this.isModalVisible = false; // Garante que modal de senha esteja fechada
    this.isCadastroModalVisible = false; // Garante que modal de cadastro esteja fechada

    if (!this.ra || this.ra.trim() === '') {
      this.loginErrorMessage = 'Por favor, digite um RA.';
      return;
    }
    this.isLoadingRA = true;
    setTimeout(() => {
      const raNormalizado = this.ra.trim();
      if (this.validRAs.includes(raNormalizado)) {
        // RA VÁLIDO: Navega para empréstimo notebook
        console.log('RA válido:', raNormalizado);
        this.router.navigate(['/emprestimo-notebook']);
      } else {
        // RA INVÁLIDO: Abre modal de confirmação de cadastro
        console.warn('RA inválido:', raNormalizado);
        this.raParaCadastrar = raNormalizado; // Guarda o RA inválido
        this.abrirCadastroModal();          // Abre a nova modal
        // Não definimos mais loginErrorMessage aqui
      }
      this.isLoadingRA = false;
    }, 300);
  }

  // --- Métodos Modal Senha (Empréstimos Ativos) ---
  abrirModalSenha(): void {
    this.loginErrorMessage = ''; // Limpa erros de RA
    this.isCadastroModalVisible = false; // Garante que outra modal esteja fechada
    this.enteredPassword = '';
    this.modalErrorMessage = '';
    this.isModalVisible = true;
  }
  fecharModalSenha(): void { this.isModalVisible = false; }
  verificarSenhaEEntrarEmprestimosAtivos(): void {
    if (this.enteredPassword === this.CORRECT_PASSWORD) {
      this.fecharModalSenha();
      this.router.navigate(['/emprestimos-ativos']);
    } else {
      this.modalErrorMessage = 'Senha incorreta. Tente novamente.';
      this.enteredPassword = '';
    }
  }

  // --- NOVO: Métodos Modal Confirmação de Cadastro ---
  abrirCadastroModal(): void {
    this.isModalVisible = false; // Garante que outra modal esteja fechada
    this.isCadastroModalVisible = true; // Abre a modal de cadastro
  }
  cancelarCadastro(): void {
    this.isCadastroModalVisible = false;
    this.raParaCadastrar = ''; // Limpa o RA guardado
  }
  confirmarCadastro(): void {
    this.isCadastroModalVisible = false;
    // Navega para a página de cadastro, passando o RA como parâmetro de rota
    this.router.navigate(['/cadastro-usuario', this.raParaCadastrar]);
    this.raParaCadastrar = ''; // Limpa o RA guardado
  }
}