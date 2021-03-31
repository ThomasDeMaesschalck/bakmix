import { Component, OnInit } from '@angular/core';
import {Order} from '../order';
import {OrderService} from '../order.service';
import {Orderstatus} from "../orderstatus";
import {Observable} from "rxjs";
import {IngredientFilter} from "../../ingredient/ingredient-filter";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html'
})
export class OrderListComponent implements OnInit {

  total$: Observable<number>;
  filter = new IngredientFilter();
  selectedOrder: Order;
  feedback: any = {};

  get orderList(): Order[] {
    return this.orderService.orderList;
  }

  constructor(private orderService: OrderService) {
  }

  ngOnInit() {
    this.filter.id = '';
    this.filter.page = 0;
    this.filter.size = 10;
    this.search();
  }

  search(): void {
    this.orderService.load(false, this.filter);
    this.total$ = this.orderService.size$;
  }

  select(selected: Order): void {
    this.selectedOrder = selected;
  }
  getStatusType(status: number): string{
    return Orderstatus[status];
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
