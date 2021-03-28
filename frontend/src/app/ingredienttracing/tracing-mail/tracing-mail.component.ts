import { Component, OnInit } from '@angular/core';
import {Order} from "../../order/order";
import {ActivatedRoute, Router} from "@angular/router";
import {OrderService} from "../../order/order.service";

@Component({
  selector: 'app-tracing-mail',
  templateUrl: './tracing-mail.component.html',
})
export class TracingMailComponent implements OnInit {

  orderList: Order[];
  feedback: any = {};
  uniqueCode: string = this.route.snapshot.paramMap.get('uniqueCode');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    const uniqueCode = this.route.snapshot.paramMap.get('uniqueCode');

    this.orderService.findTracingsByUniqueCode(uniqueCode).subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

}
