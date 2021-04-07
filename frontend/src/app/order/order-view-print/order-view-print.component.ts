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

  makeLabel(): void {
    const type = 'label';
    this.printPdf(type);
  }

      makeInvoice(): void{
        const type = 'invoice';
        this.printPdf(type);
      }

      printPdf(type: string){
        this.orderService.getPDF(this.order, type)
          .subscribe(
            (data: Blob) => {
              var file = new Blob([data], { type: 'application/pdf' });
              var fileURL = URL.createObjectURL(file);

// if you want to open PDF in new tab
              window.open(fileURL);
              var a         = document.createElement('a');
              a.href        = fileURL;
              a.target      = '_blank';
              a.download    = 'label' + this.order.id + '.pdf';
              document.body.appendChild(a);
              a.click();
              setTimeout(() => {this.feedback = {}; }, 500);
              this.feedback = {type: 'success', message: 'Label aan het maken'};
            },
            (error) => {
              console.log('getPDF error: ', error);
              this.feedback = {type: 'error', message: 'Error making label'};
            }
          );
      }
}
