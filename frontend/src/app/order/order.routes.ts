import { Routes } from '@angular/router';
import {OrderListComponent} from './order-list/order-list.component';
import {OrderViewComponent} from './order-view/order-view.component';
import {OrderlineViewComponent} from './orderline-view/orderline-view.component';

export const ORDER_ROUTES: Routes = [
  {
    path: 'orders',
    component: OrderListComponent,
  },
  {
    path: 'orders/:id',
    component: OrderViewComponent
  },
  {
    path: 'orders/:id/orderlines/:olId',
    component: OrderlineViewComponent
  }
];
