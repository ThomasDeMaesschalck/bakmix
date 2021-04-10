import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Trackingmail} from './trackingmail';
import {Order} from '../order/order';

const headers = new HttpHeaders().set('Accept', 'application/json');


@Injectable({
  providedIn: 'root'
})
export class MailService {

  apiTrackingMail = 'http://localhost:7801/api/trackingmail/';

  constructor(private http: HttpClient) {
  }

  sendTrackingMail(order: Order, trackingMail: Trackingmail) {
    let params;
    params = new HttpParams().set('id', order.id.toString());
    return this.http.post<Trackingmail>(this.apiTrackingMail, trackingMail,
      {headers, params});
  }
}