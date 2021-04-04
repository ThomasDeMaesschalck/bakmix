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

  constructor(private readonly oauthService: OAuthService) {
    this.validAccessToken();
  }

    public login() {
    this.oauthService.initLoginFlow();
  }

  public logoff() {
    this.oauthService.logOut();
  }

  public validAccessToken() {
    this.hasValidAccessToken = this.oauthService.hasValidAccessToken();
    console.log(this.hasValidAccessToken);
  }

}
