import {Component, Injectable, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { IngredientService } from '../ingredient.service';
import { Ingredient } from '../ingredient';
import { map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-ingredient-edit',
  templateUrl: './ingredient-edit.component.html'
})
export class IngredientEditComponent implements OnInit {

  id: string;
  ingredient: Ingredient;
  feedback: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ingredientService: IngredientService) {
  }

  ngOnInit() {
    this
      .route
      .params
      .pipe(
        map(p => p.id),
        switchMap(id => {
          if (id === 'new') { return of(new Ingredient()); }
          return this.ingredientService.findById(id);
        })
      )
      .subscribe(ingredient => {
          this.ingredient = ingredient;
          this.feedback = {};
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error loading'};
        }
      );
  }

  save() {
    const ingredientFound = new Ingredient();
    ingredientFound.uniqueCode = 'duplicate';

    if ((this.ingredientService.duplicateUniqueIdCheckBeforeSave(this.ingredient.uniqueCode))
      || (this.ingredient.id))
    {
    this.ingredientService.save(this.ingredient).subscribe(
      ingredient => {
        this.ingredient = ingredient;
        this.feedback = {type: 'success', message: 'Save was successful!'};
        setTimeout(() => {
          this.router.navigate(['/ingredients']);
        }, 1000);
      },
      err => {
        this.feedback = {type: 'warning', message: 'Error saving'};
      }
    );
  }
    this.feedback = {type: 'warning', message: 'Unique id was not unique'};
    setTimeout(() => {
      this.cancel();
    }, 1000);
  }

  cancel() {
    this.router.navigate(['/ingredients']);
  }

}
