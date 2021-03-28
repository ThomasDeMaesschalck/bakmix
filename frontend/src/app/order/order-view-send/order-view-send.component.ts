import { Component, OnInit } from '@angular/core';
import {Order} from "../order";
import {ActivatedRoute, Router} from "@angular/router";
import {OrderService} from "../order.service";
import {map, switchMap} from "rxjs/operators";
import {of} from "rxjs";
import {NgForm} from "@angular/forms";
import {HttpClient, HttpHeaders} from "@angular/common/http";

const headers = new HttpHeaders().set('Accept', 'application/json');

@Component({
  selector: 'app-order-view-send',
  templateUrl: './order-view-send.component.html',
})
export class OrderViewSendComponent implements OnInit {
  id: string;
  order: Order;
  feedback: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private http: HttpClient
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

  onSubmit(trackingCodeForm: NgForm) {
    if (trackingCodeForm.valid) {
      const tracking = trackingCodeForm.value;
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      this.http.post('https://formspree.io/f/mknkwyyz',
        { trackingCode: tracking.name, link: tracking.link },
        { 'headers': headers }).subscribe(
        response => {
          console.log(response);
        }
      );
    }
  }
}
