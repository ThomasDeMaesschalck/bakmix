import { Routes } from '@angular/router';
import {OrderListComponent} from './order-list/order-list.component';
import {OrderViewComponent} from './order-view/order-view.component';

export const ORDER_ROUTES: Routes = [
  {
    path: 'orders',
    component: OrderListComponent,
  },
  {
    path: 'orders/:id',
    component: OrderViewComponent
  }
];
