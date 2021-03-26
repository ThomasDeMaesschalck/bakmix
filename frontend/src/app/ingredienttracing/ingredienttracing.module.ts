import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {IngredienttracingService} from './ingredienttracing.service';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [IngredienttracingService]
})
export class IngredienttracingModule { }
