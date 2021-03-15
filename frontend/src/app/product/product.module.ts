import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductService } from './product.service';
import { PRODUCT_ROUTES } from './product.routes';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(PRODUCT_ROUTES)
  ],
  declarations: [
    ProductListComponent
  ],
  providers: [ProductService],
  exports: []
})
export class ProductModule { }
