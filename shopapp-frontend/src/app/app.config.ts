import { ApplicationConfig, Provider, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { HttpClientModule } from '@angular/common/http';

const tokenInterceptorProvider: Provider = 
{ provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }

export const appConfig: ApplicationConfig = {
  providers: [ provideRouter(routes), 
    provideHttpClient(withFetch()),
    tokenInterceptorProvider,
    provideClientHydration(),
    importProvidersFrom(HttpClientModule)
  ]
};
