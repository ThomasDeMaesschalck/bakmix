import { Customer } from '../models/customer';
import { CustomerFilter } from './customer-filter';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {map} from 'rxjs/operators';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class CustomerService {
  size$ = new BehaviorSubject<number>(0);
  customerList: Customer[] = [];
  api = 'http://localhost:7779/api/customers/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Customer> {
    const url = `${this.api}${id}`;
    const params = { id };
    return this.http.get<Customer>(url, {params, headers});
  }

  load(filter: CustomerFilter): void {
    this.find(filter).subscribe(result => {
        this.customerList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(filter: CustomerFilter): Observable<Customer[]> {

    const params: any = {
      id: filter.id,
      pageSize: filter.size,
      pageNo: filter.page
    };

    return this.http.get<Customer[]>(this.api, {headers, params}).pipe(
      map((response: any) => {
          this.size$.next(response.totalElements);
          return response.content;
        }
      ));
  }

}

