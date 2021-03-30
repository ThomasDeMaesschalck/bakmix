import { Ingredient } from './ingredient';
import { IngredientFilter } from './ingredient-filter';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {defaultIfEmpty, isEmpty, map} from 'rxjs/operators';

const headers = new HttpHeaders().set('Accept', 'application/json');


@Injectable()
export class IngredientService {
  ingredientList: Ingredient[] = [];
  ingredientSingleList: Ingredient[] = [];
  api = 'http://localhost:7777/api/ingredients/';
  uniqueCodeApi = 'http://localhost:7777/api/ingredients/uniquecode/';

  constructor(private http: HttpClient) {
  }

  findById(id: string): Observable<Ingredient> {
    const url = `${this.api}/${id}`;
    const params = { id };
    return this.http.get<Ingredient>(url, {params, headers});
  }

  findByUniqueCode(id: string): Observable<Ingredient> {
    const url = `${this.uniqueCodeApi}/${id}`;
    const params = { id };
    return this.http.get<Ingredient>(url, {params, headers});
  }

  load(filter: IngredientFilter): void {

    if (filter.id === '') {
      this.find(filter).subscribe(result => {
          this.ingredientList = result;
        },
        err => {
          console.error('error loading', err);
        }
      );
    }
    else
      {

      this.findByUniqueCode(filter.id).subscribe(result => {
        this.ingredientSingleList = [];
        this.ingredientSingleList.push(result);
        this.ingredientList = this.ingredientSingleList;
        },
        err => {
          console.error('error loading', err);
          alert('Unieke code niet gevonden, probeer opnieuw');
        }
      );
    }
  }

  find(filter: IngredientFilter): Observable<Ingredient[]> {
    const params = {
      id: filter.id,
    };
    return this.http.get<Ingredient[]>(this.api, {params, headers});
  }

  save(entity: Ingredient): Observable<Ingredient> {
    let params = new HttpParams();
    let url = '';
    if (entity.id) {
      url = `${this.api}/${entity.id.toString()}`;
      params = new HttpParams().set('ID', entity.id.toString());
      return this.http.put<Ingredient>(url, entity, {headers, params});
    } else {
      url = `${this.api}`;
      entity.available = true;
      return this.http.post<Ingredient>(url, entity, {headers, params});
    }
  }

  switchIngredientAvailability(ingredient: Ingredient): Observable<Ingredient> {
    let params = new HttpParams();
    let url = '';
    ingredient.available = !ingredient.available;
    url = `${this.api}/${ingredient.id.toString()}`;
    params = new HttpParams().set('ID', ingredient.id.toString());
    return this.http.put<Ingredient>(url, ingredient, {headers, params});
  }

  delete(entity: Ingredient): Observable<Ingredient> {
    let params = new HttpParams();
    let url = '';
    if (entity.id) {
      url = `${this.api}/${entity.id.toString()}`;
      params = new HttpParams().set('ID', entity.id.toString());
      return this.http.delete<Ingredient>(url, {headers, params});
    }
    return null;
  }
}

