import { Component, OnInit } from '@angular/core';
import { IngredientFilter } from '../ingredient-filter';
import { IngredientService } from '../ingredient.service';
import { Ingredient } from '../../models/ingredient';
import {Observable} from "rxjs";

@Component({
  selector: 'app-ingredient',
  templateUrl: 'ingredient-list.component.html'
})
export class IngredientListComponent implements OnInit {

  total$: Observable<number>;
  filter = new IngredientFilter();
  selectedIngredient: Ingredient;
  public feedback: any = {};

  get ingredientList(): Ingredient[] {
    return this.ingredientService.ingredientList;
  }

  constructor(private ingredientService: IngredientService) {
  }

  ngOnInit() {
    this.filter.id = '';
    this.filter.page = 0;
    this.filter.size = 10;
    this.search();
    this.feedback = {};
  }

  search(): void {
    this.feedback = {};
    this.ingredientService.load(this.filter);
    this.total$ = this.ingredientService.size$;
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


  onChange(pageSize: number) {
    this.filter.size = pageSize;
    this.filter.page = 0;
    this.search();
  }

  onPageChange(page: number) {
    this.filter.page = page - 1;
    this.search();
    this.filter.page = page;
  }
  }
