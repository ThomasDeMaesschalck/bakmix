import {Injectable, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { IngredientListComponent } from './ingredient-list/ingredient-list.component';
import { IngredientEditComponent } from './ingredient-edit/ingredient-edit.component';
import {IngredientService} from './ingredient.service';
import { INGREDIENT_ROUTES } from './ingredient.routes';
import {NgbDateAdapter, NgbDateParserFormatter, NgbDatepickerModule} from "@ng-bootstrap/ng-bootstrap";
import {CustomAdapter, CustomDateParserFormatter} from "../formatting/ngbDateStringAdapter";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(INGREDIENT_ROUTES),
    NgbDatepickerModule
  ],
  declarations: [
    IngredientListComponent,
    IngredientEditComponent
  ],
  providers: [IngredientService,
    {provide: NgbDateAdapter, useClass: CustomAdapter},
    {provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter}
    ],
  exports: []
})

export class IngredientModule { }
