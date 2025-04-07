import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { APP_ROUTES } from './app.routes'; // <-- Importar suas rotas

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(APP_ROUTES) // <-- Configurar as rotas aqui
    // Outros providers globais podem vir aqui, ex: provideHttpClient()
  ]
};