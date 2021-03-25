import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {IngredienttracingFilter} from './ingredienttracing-filter';
import {Ingredienttracing} from './ingredienttracing';

const headers = new HttpHeaders().set('Accept', 'application/json');


@Injectable()
export class IngredienttracingService {
  ingredienttracingList: Ingredienttracing[] = [];
  ingredienttracingListForOrderline: Ingredienttracing[] = [];
  api = 'http://localhost:7771/api/ingredienttracings/';

  constructor(private http: HttpClient) {
  }

  findByOrderlineId(id: string): Observable<Ingredienttracing> {
    const url = `${this.api}/orderline/${id}`;
    const params = { id: id };
    return this.http.get<Ingredienttracing>(url, {params, headers});
  }

  load(filter: IngredienttracingFilter): void {
    this.find(filter).subscribe(result => {
        this.ingredienttracingList = result;
      },
      err => {
        console.error('error loading', err);
      }
    );
  }

  find(filter: IngredienttracingFilter): Observable<Ingredienttracing[]> {
    const params = {
      'id': filter.id,
    };

    return this.http.get<Ingredienttracing[]>(this.api, {params, headers});
  }

  save(entity: Ingredienttracing): Observable<Ingredienttracing> {
    let params = new HttpParams();
    let url = '';
    if (entity.id) {
      url = `${this.api}/${entity.id.toString()}`;
      params = new HttpParams().set('ID', entity.id.toString());
      return this.http.put<Ingredienttracing>(url, entity, {headers, params});
    } else {
      url = `${this.api}`;
      return this.http.post<Ingredienttracing>(url, entity, {headers, params});
    }
  }

  delete(entity: Ingredienttracing): Observable<Ingredienttracing> {
    let params = new HttpParams();
    let url = '';
    if (entity.id) {
      url = `${this.api}/${entity.id.toString()}`;
      params = new HttpParams().set('ID', entity.id.toString());
      return this.http.delete<Ingredienttracing>(url, {headers, params});
    }
    return null;
  }
}

