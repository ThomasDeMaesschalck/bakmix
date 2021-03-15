import { Component, OnInit } from '@angular/core';
import { CustomerFilter } from '../customer-filter';
import { CustomerService } from '../customer.service';
import { Customer } from '../customer';

@Component({
  selector: 'app-customer',
  templateUrl: 'customer-list.component.html'
})
export class CustomerListComponent implements OnInit {

  filter = new CustomerFilter();
  selectedCustomer: Customer;
  feedback: any = {};

  get customerList(): Customer[] {
    return this.customerService.customerList;
  }

  constructor(private customerService: CustomerService) {
  }

  ngOnInit() {
    this.search();
  }

  search(): void {
    this.customerService.load(this.filter);
  }

  select(selected: Customer): void {
    this.selectedCustomer = selected;
  }
}
