import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../order.service';
import {Order} from '../order';
import {map, switchMap} from "rxjs/operators";
import {of} from "rxjs";

@Component({
  selector: 'app-order-view-print',
  templateUrl: './order-view-print.component.html'
})
export class OrderViewPrintComponent implements OnInit {

  id: string;
  order: Order;
  feedback: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    this
      .route
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

}
