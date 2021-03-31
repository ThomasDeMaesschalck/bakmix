import { Component, OnInit } from '@angular/core';
import {Order} from '../order';
import {OrderService} from '../order.service';
import {Orderstatus} from "../orderstatus";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html'
})
export class OrderListComponent implements OnInit {

   selectedOrder: Order;
  feedback: any = {};

  get orderList(): Order[] {
    return this.orderService.orderList;
  }

  constructor(private orderService: OrderService) {
  }

  ngOnInit() {
    this.search();
  }

  search(): void {
    this.orderService.load(false);
  }

  select(selected: Order): void {
    this.selectedOrder = selected;
  }
  getStatusType(status: number): string{
    return Orderstatus[status];
  }
}
