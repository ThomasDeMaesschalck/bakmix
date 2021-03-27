import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {IngredienttracingService} from './ingredienttracing.service';
import {FormsModule} from '@angular/forms';
import { TracingViewComponent } from './tracing-view/tracing-view.component';
import {RouterModule} from '@angular/router';
import {TRACING_ROUTES} from './ingredienttracing.routes';
import {OrderService} from '../order/order.service';
import {OrderlineService} from '../order/orderline.service';


@NgModule({
  declarations: [TracingViewComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(TRACING_ROUTES)

  ],
  providers: [IngredienttracingService, OrderService, OrderlineService]
})
export class IngredienttracingModule { }
