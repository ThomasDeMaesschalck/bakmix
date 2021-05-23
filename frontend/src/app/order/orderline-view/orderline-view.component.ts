import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {OrderlineService} from '../orderline.service';
import {Orderline} from '../../models/orderline';
import {IngredienttracingService} from '../../ingredienttracing/ingredienttracing.service';
import {Ingredient} from '../../models/ingredient';
import {Ingredienttracing} from '../../models/ingredienttracing';
import {IngredientService} from '../../ingredient/ingredient.service';
import {Order} from '../../models/order';
import {OrderService} from '../order.service';

@Component({
  selector: 'app-orderline-view',
  templateUrl: './orderline-view.component.html'
})
export class OrderlineViewComponent implements OnInit {

  id: string;
  olId: string;
  orderline: Orderline;
  ingredients: Ingredient | Ingredient[];
  feedback: any = {};
  feedbackAdd: any = {};
  order: Order;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderlineService: OrderlineService,
    private ingredienttracingService: IngredienttracingService,
    private ingredientService: IngredientService,
    private orderService: OrderService
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
    this.route
      .params
      .pipe(
        map(p => p.id),
        switchMap(id => {
          if (id === 'new') { return of(new Order()); }
          return this.orderService.findById(id);
        })
      )
      .subscribe(order => {
          this.order = order;
          this.feedback = {};
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
