import { Routes } from '@angular/router';
import { CustomerListComponent } from './customer-list/customer-list.component';

export const CUSTOMER_ROUTES: Routes = [
  {
    path: 'customers',
    component: CustomerListComponent
  }
];
