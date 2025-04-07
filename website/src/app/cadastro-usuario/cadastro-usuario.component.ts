// src/app/cadastro-usuario/cadastro-usuario.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router'; // Importar ActivatedRoute e Router
import { Subscription } from 'rxjs'; // Para gerenciar a inscrição

@Component({
  selector: 'app-cadastro-usuario',
  standalone: true,
  imports: [ CommonModule, FormsModule, RouterLink ],
  templateUrl: './cadastro-usuario.component.html',
  styleUrls: ['./cadastro-usuario.component.css']
})
export class CadastroUsuarioComponent implements OnInit, OnDestroy {
  // Propriedades para o formulário
  ra: string = '';
  nome: string = '';
  email: string = '';
  telefone: string = '';
  curso: string = '';

  mensagemStatus: string = '';
  isLoading: boolean = false;
  private routeSubscription: Subscription | null = null; // Para desinscrever depois

  constructor(
    private route: ActivatedRoute, // Para ler parâmetros da rota
    private router: Router         // Para navegação opcional após cadastro
  ) {}

  ngOnInit(): void {
    // Inscreve-se para ouvir mudanças nos parâmetros da rota (especificamente 'ra')
    this.routeSubscription = this.route.paramMap.subscribe(params => {
      const raFromRoute = params.get('ra'); // Pega o valor do parâmetro 'ra' da URL
      if (raFromRoute) {
        this.ra = raFromRoute;
        console.log('RA recebido para cadastro:', this.ra);
      } else {
        // Caso não venha RA (pouco provável com a rota definida), redireciona ou mostra erro
        console.error('Nenhum RA fornecido na rota para cadastro.');
        this.router.navigate(['/']); // Exemplo: volta para home
      }
    });
  }

  ngOnDestroy(): void {
    // Boa prática: cancelar a inscrição ao destruir o componente para evitar vazamentos de memória
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  cadastrarUsuario(): void {
    this.mensagemStatus = '';
    // Validação básica (pode ser mais robusta)
    if (!this.nome || !this.email || !this.ra) {
      this.mensagemStatus = 'Erro: Nome, E-mail e RA são obrigatórios.';
      return;
    }
    this.isLoading = true;
    console.log('Tentando cadastrar usuário:', {
      ra: this.ra,
      nome: this.nome,
      email: this.email,
      telefone: this.telefone,
      curso: this.curso
    });

    // --- Simulação de Chamada de API para Cadastro ---
    setTimeout(() => {
      // Aqui você chamaria seu serviço de backend para salvar o usuário.
      // Exemplo de resposta simulada:
      const sucesso = true; // Simula sucesso ou falha do backend

      if (sucesso) {
        this.mensagemStatus = `Sucesso: Usuário com RA ${this.ra} cadastrado!`;
        // Opcional: Redirecionar para outra página ou limpar formulário
        // this.router.navigate(['/']); // Exemplo: volta pra home
        // Ou apenas limpar para novo cadastro (menos comum)
        // this.limparFormulario();
      } else {
        this.mensagemStatus = 'Erro: Falha ao cadastrar usuário. Tente novamente.';
      }
      this.isLoading = false;
    }, 1000); // Simula 1 segundo de espera
  }

  // Opcional: Método para limpar o formulário
  // limparFormulario(): void {
  //   // Não limpa o RA, pois ele veio da rota
  //   this.nome = '';
  //   this.email = '';
  //   this.telefone = '';
  //   this.curso = '';
  //   this.mensagemStatus = '';
  // }
}