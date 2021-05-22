import { Component, OnInit } from '@angular/core';
import { CustomerFilter } from '../customer-filter';
import { CustomerService } from '../customer.service';
import { Customer } from '../../models/customer';
import {Observable} from "rxjs";

@Component({
  selector: 'app-customer',
  templateUrl: 'customer-list.component.html'
})
export class CustomerListComponent implements OnInit {

  total$: Observable<number>;
  filter = new CustomerFilter();
  selectedCustomer: Customer;
  feedback: any = {};

  get customerList(): Customer[] {
    return this.customerService.customerList;
  }

  constructor(private customerService: CustomerService) {
  }

  ngOnInit() {
    this.filter.id = '';
    this.filter.page = 0;
    this.filter.size = 10;
    this.search();
  }

  search(): void {
    this.customerService.load(this.filter);
    this.total$ = this.customerService.size$;
  }

  select(selected: Customer): void {
    this.selectedCustomer = selected;
  }

  onChange(pageSize: number) {
    this.filter.size = pageSize;
    this.filter.page = 0;
    this.search();
  }

  onPageChange(page: number) {
    this.filter.page = page - 1;
    this.search();
    this.filter.page = page;
  }
}
