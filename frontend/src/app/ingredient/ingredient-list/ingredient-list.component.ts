import { Component, OnInit } from '@angular/core';
import { IngredientFilter } from '../ingredient-filter';
import { IngredientService } from '../ingredient.service';
import { Ingredient } from '../ingredient';

@Component({
  selector: 'app-ingredient',
  templateUrl: 'ingredient-list.component.html'
})
export class IngredientListComponent implements OnInit {

  filter = new IngredientFilter();
  selectedIngredient: Ingredient;
  feedback: any = {};

  get ingredientList(): Ingredient[] {
    return this.ingredientService.ingredientList;
  }

  constructor(private ingredientService: IngredientService) {
  }

  ngOnInit() {
    this.search();
    this.feedback = {};
  }

  search(): void {
    this.ingredientService.load(this.filter);
  }

  select(selected: Ingredient): void {
    this.selectedIngredient = selected;
  }

  switchIngredientStatus(ingredient: Ingredient): void {
    this.ingredientService.switchIngredientAvailability(ingredient).subscribe(() => {
        this.feedback = {type: 'success', message: 'Status gewijzigd!'};
        setTimeout(() => {
          this.ngOnInit();
        }, 1000);
      },
      err => {
        this.feedback = {type: 'warning', message: 'Fout bij status wijzigen'};
      }
    );
  }

  delete(ingredient: Ingredient): void {
    if (confirm('Are you sure?')) {
      this.ingredientService.delete(ingredient).subscribe(() => {
          this.feedback = {type: 'success', message: 'Delete was successful!'};
          setTimeout(() => {
            this.ngOnInit();
          }, 1000);
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error deleting.'};
        }
      );
    }
  }

  }
