import { Customer } from './customer';
import { CustomerFilter } from './customer-filter';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class CustomerService {
  customerList: Customer[] = [];
  api = 'http://localhost:7779/api/customers/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Customer> {
    const url = `${this.api}/${id}`;
    const params = { id: id };
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
    const params = {
      'id': filter.id,
    };

    return this.http.get<Customer[]>(this.api, {params, headers});
  }

}

