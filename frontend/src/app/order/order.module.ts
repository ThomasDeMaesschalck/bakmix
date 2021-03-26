import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderListComponent } from './order-list/order-list.component';
import { OrderViewComponent } from './order-view/order-view.component';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {ORDER_ROUTES} from './order.routes';
import {OrderService} from './order.service';
import { OrderlineViewComponent } from './orderline-view/orderline-view.component';
import {OrderlineService} from './orderline.service';
import {IngredienttracingService} from '../ingredienttracing/ingredienttracing.service';


@NgModule({
  declarations: [OrderListComponent, OrderViewComponent, OrderlineViewComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(ORDER_ROUTES)
  ],

  providers: [OrderService, OrderlineService, IngredienttracingService],
  exports: []
})
export class OrderModule { }
