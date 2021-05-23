import {NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { IngredientListComponent } from './ingredient-list/ingredient-list.component';
import { IngredientEditComponent } from './ingredient-edit/ingredient-edit.component';
import {IngredientService} from './ingredient.service';
import { INGREDIENT_ROUTES } from './ingredient.routes';
import {NgbDatepickerModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(INGREDIENT_ROUTES),
    NgbModule,
    NgbDatepickerModule,
  ],
  declarations: [
    IngredientListComponent,
    IngredientEditComponent
  ],
  providers: [IngredientService
    ],
  exports: []
})

export class IngredientModule { }
