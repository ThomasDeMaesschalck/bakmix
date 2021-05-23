import {Component, Input, OnInit} from '@angular/core';
import {Order} from '../../models/order';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../order.service';
import {map, switchMap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {Orderstatus} from '../orderstatus';

@Component({
  selector: 'app-order-view',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit {

  total$: Observable<number>;
  id: string;
  order: Order;
  feedback: any = {};
  @Input() orderCurrent: Order;

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
  getStatusType(status: number): string{
    return Orderstatus[status];
  }
}
