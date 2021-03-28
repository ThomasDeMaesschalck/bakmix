import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {map, switchMap} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {OrderlineService} from '../orderline.service';
import {Orderline} from '../orderline';
import {IngredienttracingService} from '../../ingredienttracing/ingredienttracing.service';
import {Ingredient} from '../../ingredient/ingredient';
import {Ingredienttracing} from '../../ingredienttracing/ingredienttracing';
import {IngredientService} from '../../ingredient/ingredient.service';

@Component({
  selector: 'app-order-view',
  templateUrl: './orderline-view.component.html'
})
export class OrderlineViewComponent implements OnInit {

  id: string;
  olId: string;
  orderline: Orderline;
  ingredients: Ingredient | Ingredient[];
  feedback: any = {};
  feedbackAdd: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderlineService: OrderlineService,
    private ingredienttracingService: IngredienttracingService,
    private ingredientService: IngredientService
  ) {
  }

  ngOnInit(): void {
    this
      .route
      .params
      .pipe(
        map(p => p.olId),
        switchMap(olId => {
          if (olId === 'new') { return of(new Orderline()); }
          return this.orderlineService.findById(olId);
        })
      )
      .subscribe(orderline => {
          this.orderline = orderline;
          this.feedback = {};
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error loading'};
        }
      );

    this
      .route
      .params
      .pipe(
        map(p => p.olId),
        switchMap(olId => {
          if (olId === 'new') { return of(new Ingredient()); }
          return this.ingredienttracingService.findAvailableIngredients(olId);
        })
      )
      .subscribe(ingredient => {
          this.ingredients = ingredient;
          this.feedback = {};
          this.feedbackAdd = {};
        },
        err => {
          this.feedback = {type: 'warning', message: 'Error loading'};
        }
      );
   }

  saveTracing(ingredientId: number, orderlineId: number): void {
    const ingredienttracing = new Ingredienttracing(ingredientId, orderlineId);
    this.ingredienttracingService.saveIngredienttracing(ingredienttracing);
    setTimeout(() => {this.ngOnInit(); }, 500);
    this.feedbackAdd = {type: 'success', message: 'Gekoppeld'};

  }

  removeTracing(ingredienttracingId: number): void {
    this.ingredienttracingService.removeIngredienttracing(ingredienttracingId);
    setTimeout(() => {this.ngOnInit(); }, 500);
    this.feedback = {type: 'success', message: 'Ontkoppeld'};
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
}
