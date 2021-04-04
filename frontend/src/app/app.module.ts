import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IngredientModule } from './ingredient/ingredient.module';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ProductModule } from './product/product.module';
import { CustomerModule } from './customer/customer.module';
import {HomeModule} from './home/home.module';
import {OrderModule} from './order/order.module';
import {IngredienttracingModule} from './ingredienttracing/ingredienttracing.module';
import {OAuthModule} from 'angular-oauth2-oidc';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IngredientModule,
    HttpClientModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: ['http://localhost:7777/api', 'http://localhost:7771/api', 'http://localhost:7779/api',
          'http://localhost:7772/api', 'http://localhost:7773/api', 'http://localhost:7778/api' ],
        sendAccessToken: true
      }}),
    FormsModule,
    NgbModule,
    ProductModule,
    CustomerModule,
    HomeModule,
    OrderModule,
    IngredienttracingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
