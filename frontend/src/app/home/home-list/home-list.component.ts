import { Component, OnInit } from '@angular/core';
import {Ingredient} from '../../ingredient/ingredient';
import {IngredientService} from '../../ingredient/ingredient.service';
import {IngredientFilter} from '../../ingredient/ingredient-filter';
import {Order} from '../../order/order';
import {OrderService} from '../../order/order.service';
import {Orderstatus} from '../../order/orderstatus';

@Component({
  selector: 'app-home-list',
  templateUrl: './home-list.component.html',
  styleUrls: ['./home-list.component.css']
})
export class HomeListComponent implements OnInit {

  selectedIngredient: Ingredient;
  filter = new IngredientFilter();

  constructor(
    private ingredientService: IngredientService,
    private orderService: OrderService) {
  }
  ngOnInit(): void {
    this.searchIngredients();
    this.searchOrders();

  }

  get ingredientList(): Ingredient[] {
    return this.ingredientService.ingredientList;
  }

  searchIngredients(): void {
    this.ingredientService.load(this.filter);
  }

  select(selected: Ingredient): void {
    this.selectedIngredient = selected;
  }

  get orderList(): Order[] {
    return this.orderService.orderList;
  }

  searchOrders(): void {
    this.orderService.load();
  }
  getStatusType(status: number): string{
    return Orderstatus[status];
  }
}
