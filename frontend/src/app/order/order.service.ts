import { Order } from './order';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {Ingredient} from '../ingredient/ingredient';
import {map} from 'rxjs/operators';
import {OrderFilter} from './order-filter';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class OrderService {
  size$ = new BehaviorSubject<number>(0);
  orderList: Order[] = [];
  api = 'http://localhost:7772/api/orders/';
  apiTracedOrders = 'http://localhost:7772/api/tracing/';
  apiPdf = 'http://localhost:7800/api/pdfreport/';

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

  load(index: boolean, filter: OrderFilter): void {
    this.find(index, filter).subscribe(result => {
        this.orderList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(index: boolean, filter: OrderFilter): Observable<Order[]> {
    let params: any;
    if (index === true) {
      params = {
        'index': 'true',
        id: filter.id,
        pageSize: filter.size,
        pageNo: filter.page
      };
    } else {
      params = {
        id: filter.id,
        pageSize: filter.size,
        pageNo: filter.page
      };
    }
    return this.http.get<Order[]>(this.api, {params, headers}).pipe(
      map((response: any) => {
          this.size$.next(response.totalElements);
          return response.content;
        }
      ));
    ;
  }

  switchOrderStatus(order: Order): Observable<Order> {
    let params = new HttpParams();
    let url = '';
    if (order.status === 0) {
      order.status = 1;
    }
    url = `${this.api}${order.id.toString()}`;
    params = new HttpParams().set('ID', order.id.toString());
    return this.http.put<Order>(url, order, {headers, params});
  }

  getPDF(order: Order, type: string): Observable<Blob>
  {
    let params: any;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType : 'blob'});
    if (type === 'label')
   {
    params = {
      type: 'label',
      id: order.id,
    };
   }
   else if (type === 'invoice')
   {
     params = {
       type: 'invoice',
       id: order.id,
     };
   }
    return this.http.get<Blob>(this.apiPdf, { headers, responseType : 'blob' as 'json', params});
  }

}
