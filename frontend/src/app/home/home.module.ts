import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeListComponent } from './home-list/home-list.component';
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {PRODUCT_ROUTES} from "../product/product.routes";
import {HOME_ROUTES} from "./home.routes";



@NgModule({


  declarations: [HomeListComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(HOME_ROUTES)
  ]
})
export class HomeModule { }
