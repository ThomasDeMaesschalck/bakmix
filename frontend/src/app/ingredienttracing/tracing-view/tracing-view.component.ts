import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../../order/order.service';
import {Orderline} from '../../order/orderline';
import {map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {Order} from '../../order/order';
import {IngredientService} from "../../ingredient/ingredient.service";
import {Ingredient} from "../../ingredient/ingredient";

@Component({
  selector: 'app-tracing-view',
  templateUrl: './tracing-view.component.html',
})
export class TracingViewComponent implements OnInit {

  orderList: Order[];
  ingredient: Ingredient;
  feedback: any = {};
  uniqueCode: string = this.route.snapshot.paramMap.get('uniqueCode');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private ingredientService: IngredientService
  ) { }

  ngOnInit(): void {
    const uniqueCode = this.route.snapshot.paramMap.get('uniqueCode');

    this.orderService.findTracingsByUniqueCode(uniqueCode).subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );

    this.ingredientService.findByUniqueCode(uniqueCode).subscribe(result => {
        this.ingredient = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

}
