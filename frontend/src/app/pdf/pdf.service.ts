import { Injectable } from '@angular/core';
import {Order} from '../order/order';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  apiPdfLabel = 'http://localhost:7800/api/pdflabel/';
  apiPdfInvoice = 'http://localhost:7800/api/pdfinvoice/';

  constructor(private http: HttpClient) {
  }
  getPDF(order: Order, type: string): Observable<Blob>
  {
    let params: any;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType : 'blob'});
    params = { id: order.id };

    if (type === 'label')
    {
      return this.http.get<Blob>(this.apiPdfLabel, { headers, responseType : 'blob' as 'json', params});

    }
    else if (type === 'invoice')
    {
      return this.http.get<Blob>(this.apiPdfInvoice, { headers, responseType : 'blob' as 'json', params});
    }
  }
}
