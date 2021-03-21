import {Customer} from '../customer/customer';

export class Order {
  id: number;
  customerId: number;
  status: number;
  discount: number;
  shippingCost: number;

  customer: Customer;
}
