import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {Orderline} from './orderline';
import {Ingredient} from '../ingredient/ingredient';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class OrderlineService {
  orderline: Orderline;
  api = 'http://localhost:7772/api/orders/orderlinewithlinkedingredients/';

  constructor(private http: HttpClient) {
  }

  findById(olId: string): Observable<Orderline> {
    const url = `${this.api}${olId}`;
    const params = { olId: olId };
    return this.http.get<Orderline>(url, {params, headers});
  }

  load(): void {
    this.find().subscribe(result => {
        this.orderline = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(): Observable<Orderline> {
    const params = {
      id: ''
    };

    return this.http.get<Orderline>(this.api, {params, headers});
  }
}

