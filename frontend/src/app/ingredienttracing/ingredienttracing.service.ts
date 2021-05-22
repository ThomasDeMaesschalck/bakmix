import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {Ingredient} from '../models/ingredient';
import {Ingredienttracing} from '../models/ingredienttracing';
import {Router} from '@angular/router';
import {Order} from '../models/order';

const headers = new HttpHeaders().set('Accept', 'application/json');


@Injectable()
export class IngredienttracingService {
  orders: Order[];
  availableIngredientsForOrderline: Ingredient[] = [];
  apiOrderlines = 'http://localhost:7772/api/orders/availableingredientsfororderline/';
  apiIngredienttracing = 'http://localhost:7771/api/ingredienttracings/';

  constructor(private http: HttpClient) {
  }

  findAvailableIngredients(olId: string): Observable<Ingredient[]> {
    const url = `${this.apiOrderlines}${olId}`;
    const params = { olId: olId };
    return this.http.get<Ingredient[]>(url, {params, headers});
  }

  load(): void {
    this.find().subscribe(result => {
        this.availableIngredientsForOrderline = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(): Observable<Ingredient[]> {

    return this.http.get<Ingredient[]>(this.apiOrderlines, {headers});
  }

  saveIngredienttracing(ingredienttracing: Ingredienttracing): void {
    const params = new HttpParams();
    const url = `${this.apiIngredienttracing}`;
    this.http.post<Ingredienttracing>(url, ingredienttracing, {headers, params}).subscribe(result =>
      this.findAvailableIngredients(ingredienttracing.orderlineId.toString()));
  }

  removeIngredienttracing(ingredienttracingId: number): void {
    const params = new HttpParams();
    const url = `${this.apiIngredienttracing}${ingredienttracingId}`;
    this.http.delete<Ingredienttracing>(url, {headers, params}).subscribe(result =>
      this.find());

  }

}

