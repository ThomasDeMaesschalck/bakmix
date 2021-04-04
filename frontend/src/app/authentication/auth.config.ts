import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {

  issuer: 'http://localhost:8081/auth/realms/bakmix',
  redirectUri: window.location.origin + '/home',
  clientId: 'bakmix-frontend',
  scope: 'openid profile email offline_access bakmix',
  responseType: 'code',
  requireHttps: false,
  disableAtHashCheck: true,
  showDebugInformation: true
};

export class OAuthModuleConfig {
  resourceServer: OAuthResourceServerConfig = {sendAccessToken: false};
}

export class OAuthResourceServerConfig {
  /**
   * Urls for which calls should be intercepted.
   * If there is an ResourceServerErrorHandler registered, it is used for them.
   * If sendAccessToken is set to true, the access_token is send to them too.
   */
  allowedUrls?: Array<string>;
  sendAccessToken = true;
  customUrlValidation?: (url: string) => boolean;
}
