import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // <-- Importar RouterOutlet

@Component({
  selector: 'app-root',
  standalone: true, // <--- DEVE SER STANDALONE
  imports: [
    RouterOutlet // <-- IMPORTAR RouterOutlet AQUI
  ],
  template: `
    <h1>Meu App</h1>
    <router-outlet></router-outlet> `,
  // styleUrls: ['./app.component.css'] // Se tiver estilos
})
export class AppComponent {
  title = 'meu-app-standalone';
}