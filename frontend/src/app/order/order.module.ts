import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderListComponent } from './order-list/order-list.component';
import { OrderViewComponent } from './order-view/order-view.component';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {ORDER_ROUTES} from './order.routes';
import {OrderService} from './order.service';


@NgModule({
  declarations: [OrderListComponent, OrderViewComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(ORDER_ROUTES)
  ],

  providers: [OrderService],
  exports: []
})
export class OrderModule { }
