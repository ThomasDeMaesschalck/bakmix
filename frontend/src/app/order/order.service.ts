import { Order } from '../models/order';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {map} from 'rxjs/operators';
import {OrderFilter} from './order-filter';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class OrderService {
  size$ = new BehaviorSubject<number>(0);
  orderList: Order[] = [];
  api = 'http://localhost:7772/api/orders/';
  apiTracedOrders = 'http://localhost:7772/api/tracing/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Order> {
    const url = `${this.api}${id}`;
    const params = {id: id};
    return this.http.get<Order>(url, {params, headers});
  }

  findTracingsByUniqueCode(uniqueCode: string): Observable<Order[]> {
    const url = `${this.apiTracedOrders}${uniqueCode}`;
    const params = {uniqueCode: uniqueCode};
    return this.http.get<Order[]>(url, {params, headers});
  }

  load(filter: OrderFilter): void {
    this.find(filter).subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(filter: OrderFilter): Observable<Order[]> {
    let params: any;
    params = {
        id: filter.id,
        pageSize: filter.size,
        pageNo: filter.page
      };
    return this.http.get<Order[]>(this.api, {params, headers}).pipe(
      map((response: any) => {
          this.size$.next(response.totalElements);
          return response.content;
        }
      ));
  }

  switchOrderStatus(order: Order, send: boolean): Observable<Order> {
    let params = new HttpParams();
    let url = '';
    if (order.status === 2 && send === true) {
      order.status = 3;
    }
    url = `${this.api}${order.id.toString()}`;
    params = new HttpParams().set('ID', order.id.toString());
    return this.http.put<Order>(url, order, {headers, params});
  }
}
