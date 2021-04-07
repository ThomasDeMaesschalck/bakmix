import { NgModule, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { OAuthModule, AuthConfig } from 'angular-oauth2-oidc';

import { AuthConfigService } from './authconfig.service';
import { authConfig, OAuthModuleConfig } from './auth.config';

export function init_app(authConfigService: AuthConfigService) {
  return () => authConfigService.initAuth();
}

@NgModule({
  imports: [ HttpClientModule, OAuthModule.forRoot({
    resourceServer: {
      allowedUrls: ['http://localhost:7777/api', 'http://localhost:7771/api', 'http://localhost:7779/api',
        'http://localhost:7772/api', 'http://localhost:7773/api', 'http://localhost:7778/api', 'http://localhost:7800/api' ],
      sendAccessToken: true
    }}) ],
  providers: [
    AuthConfigService,
    { provide: AuthConfig, useValue: authConfig },
    OAuthModuleConfig,
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [ AuthConfigService ],
      multi: true
    }
  ]
})
export class AuthConfigModule { }
