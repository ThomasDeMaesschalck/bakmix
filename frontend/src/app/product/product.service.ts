import { Product } from '../models/product';
import { ProductFilter } from './product-filter';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable()
export class ProductService {
  productList: Product[] = [];
  api = 'http://localhost:7778/api/products/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Product> {
    const url = `${this.api}${id}`;
    const params = { id: id };
    return this.http.get<Product>(url, {params, headers});
  }

  load(filter: ProductFilter): void {
    this.find(filter).subscribe(result => {
        this.productList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(filter: ProductFilter): Observable<Product[]> {
    const params = {
      'id': filter.id,
    };

    return this.http.get<Product[]>(this.api, {params, headers});
  }

}

