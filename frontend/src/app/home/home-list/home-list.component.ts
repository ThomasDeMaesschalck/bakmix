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

  public feedback: any = {};
  selectedIngredient: Ingredient;
  filter = new IngredientFilter();

  constructor(
    private ingredientService: IngredientService,
    private orderService: OrderService) {
  }
  ngOnInit(): void {
    this.feedback = {};
    this.searchIngredients();
    this.searchOrders();
    this.searchExpired();
  }

  get ingredientList(): Ingredient[] {
    return this.ingredientService.ingredientList;
  }

  searchIngredients(): void {
    this.filter.size = 10;
    this.ingredientService.load(this.filter);
  }

  select(selected: Ingredient): void {
    this.selectedIngredient = selected;
  }

  get orderList(): Order[] {
    return this.orderService.orderList;
  }

  searchOrders(): void {
    this.orderService.load(true, this.filter);
  }
  getStatusType(status: number): string{
    return Orderstatus[status];
  }

  searchExpired(): void {
    this.ingredientService.loadExpired();
  }

  setIngredientUnavailable(ingredient: Ingredient): void{
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

  get expiredList(): Ingredient[] {
    return this.ingredientService.expiredList;
  }
}
