// src/app/app.routes.ts
import { Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { EmprestimosAtivosComponent } from './emprestimos-ativos/emprestimos-ativos.component';
import { EmprestimoNotebookComponent } from './emprestimo-notebook/emprestimo-notebook.component';
import { CadastroUsuarioComponent } from './cadastro-usuario/cadastro-usuario.component'; // <-- Importar novo componente

export const APP_ROUTES: Routes = [
  { path: '', component: HomeComponent, title: 'Login' },
  { path: 'home', component: HomeComponent, title: 'Login' },
  { path: 'emprestimos-ativos', component: EmprestimosAtivosComponent, title: 'Empréstimos Ativos' },
  { path: 'emprestimo-notebook', component: EmprestimoNotebookComponent, title: 'Empréstimo Notebook' },
  // ---> NOVA ROTA COM PARÂMETRO <---
  { path: 'cadastro-usuario/:ra', component: CadastroUsuarioComponent, title: 'Cadastro Usuário' }, // :ra indica um parâmetro de rota

  { path: '**', redirectTo: '', pathMatch: 'full' }
];