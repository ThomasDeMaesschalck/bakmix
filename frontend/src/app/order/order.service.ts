import { Order } from './order';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class OrderService {
  orderList: Order[] = [];
  api = 'http://localhost:7772/api/orders/';
  apiTracedOrders = 'http://localhost:7772/api/tracing/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Order> {
    const url = `${this.api}/${id}`;
    const params = { id: id };
    return this.http.get<Order>(url, {params, headers});
  }

  findTracingsByUniqueCode(uniqueCode: string): Observable<Order[]>{
    const url = `${this.apiTracedOrders}/${uniqueCode}`;
    const params = { uniqueCode: uniqueCode };
    return this.http.get<Order[]>(url, {params, headers});
  }

  load(): void {
    this.find().subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(): Observable<Order[]> {
    const params = {
      'id': ''
    };

    return this.http.get<Order[]>(this.api, {params, headers});
  }
}

