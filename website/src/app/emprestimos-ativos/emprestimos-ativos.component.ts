// src/app/emprestimos-ativos/emprestimos-ativos.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; // <-- 1. Importar RouterLink

// Opcional: Importar ActivatedRoute para ler parâmetros da URL
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs';
// import { map } from 'rxjs/operators';

@Component({
  selector: 'app-emprestimos-ativos',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink // <-- 2. Adicionar RouterLink às importações do componente
  ],
  template: `
    <div class="emprestimos-container">
      <h2>Meus Empréstimos Ativos</h2>
      <p>
        Aqui serão listados os empréstimos ativos.
        <br>
        (Implementação futura)
      </p>
      <a routerLink="/" class="btn btn-light btn-voltar">
        <i class="fas fa-arrow-left"></i> Voltar para Home
      </a>

    </div>
  `,
  styles: [`
    .emprestimos-container {
      padding: 20px;
      border: 1px solid #eee;
      border-radius: 8px;
      background-color: #fff;
      max-width: 800px; /* Limita a largura */
      margin: 20px auto; /* Centraliza */
    }
    .btn-voltar { /* Estilo para o botão de voltar */
      display: inline-block;
      margin-top: 30px; /* Mais espaço acima */
      padding: 10px 15px;
    }
    /* Classe básica para botão (se não estiver global) */
    .btn {
        display: inline-block;
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 1rem;
        text-align: center;
        text-decoration: none;
        transition: background-color 0.2s ease-in-out, border-color 0.2s ease-in-out;
    }
    .btn-light {
        background-color: #f8f9fa;
        color: #212529;
        border: 1px solid #ccc;
    }
    .btn-light:hover {
        background-color: #e2e6ea;
        border-color: #adb5bd;
    }
    /* Estilo opcional para ícone (se usar Font Awesome) */
    .btn i {
        margin-right: 5px;
    }
  `]
})
export class EmprestimosAtivosComponent {

  // // Opcional: Código para ler query param 'usuario' da URL
  // usuarioRa$: Observable<string | null>;
  // constructor(private route: ActivatedRoute) {
  //   this.usuarioRa$ = this.route.queryParamMap.pipe(
  //     map(params => params.get('usuario'))
  //   );
  // }

}