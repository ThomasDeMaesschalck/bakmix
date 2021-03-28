import { Routes } from '@angular/router';
import {OrderListComponent} from './order-list/order-list.component';
import {OrderViewComponent} from './order-view/order-view.component';
import {OrderlineViewComponent} from './orderline-view/orderline-view.component';
import {OrderViewPrintComponent} from './order-view-print/order-view-print.component';
import {OrderViewSendComponent} from './order-view-send/order-view-send.component';

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
    path: 'orders/:id/print',
    component: OrderViewPrintComponent
  },
  {
    path: 'orders/:id/send',
    component: OrderViewSendComponent
  },
  {
    path: 'orders/:id/orderlines/:olId',
    component: OrderlineViewComponent
  }
];
