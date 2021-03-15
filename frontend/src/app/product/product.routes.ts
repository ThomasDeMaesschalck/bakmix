import { Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';

export const PRODUCT_ROUTES: Routes = [
  {
    path: 'products',
    component: ProductListComponent
  }
];
