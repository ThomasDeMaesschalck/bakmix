import { Routes } from '@angular/router';
import { IngredientListComponent } from './ingredient-list/ingredient-list.component';
import { IngredientEditComponent } from './ingredient-edit/ingredient-edit.component';

export const INGREDIENT_ROUTES: Routes = [
  {
    path: 'ingredients',
    component: IngredientListComponent
  },
  {
    path: 'ingredients/:id',
    component: IngredientEditComponent
  }
];
