import {Component, Injectable, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { IngredientService } from '../ingredient.service';
import { Ingredient } from '../../models/ingredient';
import { map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import {NgbDateParserFormatter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-ingredient-edit',
  templateUrl: './ingredient-edit.component.html'
})
export class IngredientEditComponent implements OnInit {

  id: string;
  ingredient: Ingredient;
  feedback: any = {};
  purchaseDate;
  expiryDate;
  ngbDate: NgbDateStruct;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ingredientService: IngredientService,
    private parserFormatter: NgbDateParserFormatter
) {
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
          this.purchaseDate = this.parserFormatter.parse(this.ingredient.purchaseDate);
          this.expiryDate = this.parserFormatter.parse(this.ingredient.expiry);
          this.feedback = {};
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error loading'};
        }
      );
  }

  save() {
   this.ingredient.purchaseDate =  this.parserFormatter.format(this.purchaseDate);
    this.ingredient.expiry =  this.parserFormatter.format(this.expiryDate);
    this.ingredientService.save(this.ingredient).subscribe(
        ingredient => {
          this.ingredient = ingredient;
          this.feedback = {type: 'success', message: 'Save was successful!'};
          setTimeout(() => {
            this.router.navigate(['/ingredients']);
          }, 1000);
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error saving, check if unique Id is unique'};
        }
      );
    }


  cancel() {
    this.router.navigate(['/ingredients']);
  }

}
