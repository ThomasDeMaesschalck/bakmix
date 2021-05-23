import { Component, OnInit } from '@angular/core';
import {Order} from '../../models/order';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../order.service';
import {map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Trackingmail} from '../../models/trackingmail';
import {MailService} from '../../mail/mail.service';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Component({
  selector: 'app-order-view-send',
  templateUrl: './order-view-send.component.html',
})
export class OrderViewSendComponent implements OnInit {
  id: string;
  order: Order;
  feedback: any = {};
  trackingMail: Trackingmail;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private http: HttpClient,
    private mailService: MailService
  ) { }

  ngOnInit(): void {
    this.trackingMail = new Trackingmail();
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

  onSubmit() {
    this.feedback = {type: 'success', message: 'Even geduld... mail aan het sturen'};
    setTimeout(() => {this.feedback = {}; }, 5000);
    this.mailService.sendTrackingMail(this.order, this.trackingMail).subscribe(
       (trackingmail) => {
         this.trackingMail = this.trackingMail;
         this.feedback = {type: 'success', message: 'Mail zenden gelukt!'};
         this.orderService.switchOrderStatus(this.order, true).subscribe();
         setTimeout(() => {
           this.router.navigate(['/orders/' + this.order.id]);
         }, 2000);
       },
       err => {
         this.feedback = {type: 'warning', message: 'Fout bij zenden mail'};
       }
     );
  }
}
