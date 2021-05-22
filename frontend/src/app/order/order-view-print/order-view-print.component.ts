import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../order.service';
import {Order} from '../../models/order';
import {map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {PdfService} from '../../pdf/pdf.service';

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
    private orderService: OrderService,
    private pdfService: PdfService
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
    setTimeout(() => {this.feedback = {}; }, 2000);
    this.feedback = {type: 'success', message: 'PDF aan het maken'};
  }

      makeInvoice(): void{
        const type = 'invoice';
        this.printPdf(type);
        this.feedback = {type: 'success', message: 'Even geduld... PDF aan het maken'};
        setTimeout(() => {this.feedback = {}; }, 5000);
      }

      printPdf(type: string){
        this.pdfService.getPDF(this.order, type)
          .subscribe(
            (data: Blob) => {
              let file = new Blob([data], { type: 'application/pdf' });
              let fileURL = URL.createObjectURL(file);

              window.open(fileURL);
              let a         = document.createElement('a');
              a.href        = fileURL;
              a.target      = '_blank';
              if (type === 'label')
              {
              a.download    = 'label' + this.order.id + '.pdf';
                }
              else if (type === 'invoice')
              {
                a.download    = 'factuur' + this.order.id + '.pdf';
              }
              document.body.appendChild(a);
              a.click();
            },
            (error) => {
              console.log('getPDF error: ', error);
              this.feedback = {type: 'error', message: 'Fout bij maken PDF'};
            }
          );
      }
}
