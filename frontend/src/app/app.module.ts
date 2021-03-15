import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IngredientModule } from './ingredient/ingredient.module';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ProductModule } from './product/product.module';
import { CustomerModule } from './customer/customer.module';

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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
