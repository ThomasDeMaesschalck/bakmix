import { Component, OnInit } from '@angular/core';
import {Order} from '../../models/order';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../../order/order.service';
import {Ingredient} from "../../models/ingredient";
import {IngredientService} from "../../ingredient/ingredient.service";
import {MailService} from "../../mail/mail.service";

@Component({
  selector: 'app-tracing-mail',
  templateUrl: './tracing-mail.component.html',
})
export class TracingMailComponent implements OnInit {

  orderList: Order[];
  feedback: any = {};
  uniqueCode: string = this.route.snapshot.paramMap.get('uniqueCode');
  ingredient: Ingredient;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private mailService: MailService,
    private ingredientService: IngredientService
  ) { }

  ngOnInit(): void {
    const uniqueCode = this.route.snapshot.paramMap.get('uniqueCode');
    this.orderList = [];
    this.orderService.findTracingsByUniqueCode(uniqueCode).subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );

    this.ingredientService.findByUniqueCode(uniqueCode).subscribe(result => {
        this.ingredient = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  recall(): void{
    this.feedback = {type: 'success', message: 'Even geduld... mail(s) aan het verzenden'};
    this.mailService.sendRecallMail(this.uniqueCode).subscribe(
      (result) => {
        this.feedback = {type: 'success', message: 'Mail(s) zenden gelukt!'};
        this.ingredientService.recallIngredient(this.ingredient).subscribe();
        setTimeout(() => {
          this.router.navigate(['/tracing/' + this.uniqueCode]);
        }, 2000);
      },
      err => {
        this.feedback = {type: 'warning', message: 'Fout bij zenden mail(s)'};
      }
    );
  }

}
