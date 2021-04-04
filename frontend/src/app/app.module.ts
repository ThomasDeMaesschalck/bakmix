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
import {AuthConfigModule} from './authentication/auth.config.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IngredientModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    ProductModule,
    CustomerModule,
    HomeModule,
    OrderModule,
    IngredienttracingModule,
    AuthConfigModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
