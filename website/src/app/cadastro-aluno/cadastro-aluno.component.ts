import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlunoService, AlunoCreatePayload } from '../core/services/aluno.service'; // <-- 1. Importar Serviço e Payload Type
import { finalize } from 'rxjs/operators'; // Para o loading

@Component({
  selector: 'app-cadastro-aluno',
  standalone: true,
  imports: [ CommonModule, FormsModule, RouterLink ],
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

  // ---> NOVA LISTA DE CURSOS <---
  readonly cursosDisponiveis: string[] = [
    "BIOLOGIA",
    "BIOMEDICINA",
    "ENFERMAGEM",
    "FARMACIA",
    "FISIOTERAPIA",
    "FONOAUDIOLOGIA",
    "MEDICINA",
    "MEDICINA_VETERINARIA", // Atenção: na API estava como MEDICINA_VETERINARIA
    "NUTRICAO",           // Atenção: na API estava como NUTRICAO
    "ODONTOLOGIA",
    "PSICOLOGIA",
    "TERAPIA_OCUPACIONAL" // Atenção: na API estava como TERAPIA_OCUPACIONAL
  ];
  // Observação: Mantive os nomes como vieram da API (ex: MEDICINA_VETERINARIA).
  // Se precisar exibir nomes mais amigáveis (ex: "Medicina Veterinária"),
  // podemos usar um array de objetos { value: 'MEDICINA_VETERINARIA', display: 'Medicina Veterinária' }

  mensagemStatus: string = '';
  isLoading: boolean = false;
  private routeSubscription: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alunoService: AlunoService // <-- 2. Injetar AlunoService
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
    this.mensagemStatus = ''; // Limpa mensagens anteriores
    // Validação básica
    if (!this.nome || !this.email || !this.ra || !this.curso) {
      this.mensagemStatus = 'Erro: Nome, E-mail, RA e Curso são obrigatórios.';
      return;
    }

    this.isLoading = true; // Ativa o loading

    // Monta o objeto de dados para enviar à API (sem o 'id')
    const alunoPayload: AlunoCreatePayload = {
      nome: this.nome.trim(),
      ra: this.ra.trim(), // RA já veio da rota, mas faz trim por segurança
      email: this.email.trim(),
      telefone: this.telefone ? this.telefone.trim() : undefined, // Envia undefined se vazio
      curso: this.curso // Valor selecionado no dropdown
    };

    console.log('Enviando para cadastro:', alunoPayload);

    // Chama o método do serviço para cadastrar
    this.alunoService.cadastrarAluno(alunoPayload)
      .pipe(
        finalize(() => this.isLoading = false) // Desativa o loading ao finalizar (sucesso ou erro)
      )
      .subscribe({
        next: (response) => { // Callback de sucesso
          console.log('Resposta do cadastro:', response);
          // Verifica se a API retornou status OK e os dados do aluno criado
          if (response && response.status === 'OK' && response.data) {
            this.mensagemStatus = `Sucesso: Aluno "${response.data.nome}" (RA: ${response.data.ra}) cadastrado com ID: ${response.data.id}!`;
            // Opcional: Limpar formulário ou redirecionar
            // this.limparFormulario();
            // this.router.navigate(['/']); // Ex: Volta para home após sucesso
          } else {
            // Caso a API retorne status OK mas sem dados, ou outro status inesperado
            this.mensagemStatus = `Atenção: ${response?.message || 'Cadastro concluído, mas resposta inesperada da API.'}`;
          }
        },
        error: (error: Error) => { // Callback de erro (tratado no serviço)
          console.error('Erro recebido no componente:', error);
          // A mensagem de erro já vem tratada do serviço
          this.mensagemStatus = `Erro: ${error.message || 'Não foi possível cadastrar o aluno.'}`;
        }
      });
  }

  // Opcional: Método para limpar o formulário
  limparFormulario(): void {
    // Não limpa o RA, pois ele veio da rota e é identificador
    this.nome = '';
    this.email = '';
    this.telefone = '';
    this.curso = ''; // Volta para a opção default do select
    this.mensagemStatus = '';
  }
}