// src/app/emprestimo-notebook/emprestimo-notebook.component.ts
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';   // Para [(ngModel)]
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; // Para o botão Voltar

@Component({
  selector: 'app-emprestimo-notebook',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink // Importa RouterLink para o botão Voltar
  ],
  templateUrl: './emprestimo-notebook.component.html',
  styleUrls: ['./emprestimo-notebook.component.css'] // <-- Verifique se o nome está correto (styleUrls)
})
export class EmprestimoNotebookComponent {
  numeroPatrimonio: string = '';
  mensagemStatus: string = ''; // Para feedback futuro

  constructor() {} // Router pode ser injetado aqui se precisar de navegação programática

  registrarEmprestimo(): void {
    this.mensagemStatus = ''; // Limpa status anterior
    if (!this.numeroPatrimonio || this.numeroPatrimonio.trim() === '') {
        this.mensagemStatus = 'Erro: Por favor, digite o número do patrimônio.';
        return;
    }
    // --- Lógica de Registro (Simulada por enquanto) ---
    console.log('Tentando registrar empréstimo para o patrimônio:', this.numeroPatrimonio.trim());
    // Aqui você chamaria um serviço para registrar o empréstimo no backend.
    // Por enquanto, apenas exibimos uma mensagem de sucesso simulada.
    this.mensagemStatus = `Sucesso: Empréstimo para o patrimônio ${this.numeroPatrimonio.trim()} registrado (simulado).`;
    // Opcional: Limpar campo após sucesso
    // this.numeroPatrimonio = '';
  }
}