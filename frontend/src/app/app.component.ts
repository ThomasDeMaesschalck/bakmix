import { Component } from '@angular/core';
import {AuthConfig, NullValidationHandler, OAuthService} from "angular-oauth2-oidc";
import { JwksValidationHandler } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  isCollapsed = true;
  public claims: any;
  public hasValidAccessToken: boolean;

  constructor(private oauthService: OAuthService) {
    this.validAccessToken();
    this.setupAutomaticSilentRefresh();
    this.configure();
  }

  authConfig: AuthConfig = {
    issuer: 'http://localhost:8081/auth/realms/bakmix',
    redirectUri: window.location.origin + '/home',
    clientId: 'bakmix-frontend',
    scope: 'openid profile email offline_access bakmix',
    responseType: 'code',
    requireHttps: false,
    disableAtHashCheck: true,
    showDebugInformation: true
  };

  public login() {
    this.oauthService.initLoginFlow();
  }

  public logoff() {
    this.oauthService.logOut();
  }

  private configure() {
    this.oauthService.configure(this.authConfig);
    this.oauthService.tokenValidationHandler = new  NullValidationHandler();
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }

   private setupAutomaticSilentRefresh() {
     this.oauthService.setupAutomaticSilentRefresh();
   }

   public name() {
      this.claims = this.oauthService.getIdentityClaims();
      console.log(this.claims);
      if (!this.claims) return null;
      return this.claims;
 }

 public validAccessToken() {
     this.hasValidAccessToken = this.oauthService.hasValidAccessToken();
     console.log(this.hasValidAccessToken);
 }

}
