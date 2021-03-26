import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {map, switchMap} from "rxjs/operators";
import {of} from "rxjs";
import {OrderlineService} from '../orderline.service';
import {Orderline} from '../orderline';

@Component({
  selector: 'app-order-view',
  templateUrl: './orderline-view.component.html'
})
export class OrderlineViewComponent implements OnInit {

  id: string;
  olId: string;
  orderline: Orderline;
  feedback: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderlineService: OrderlineService
  ) { }

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
  }

}
