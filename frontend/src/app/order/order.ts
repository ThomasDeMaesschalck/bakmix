import {Customer} from '../customer/customer';
import {Orderline} from './orderline';

export class Order {
  id: number;
  customerId: number;
  status: number;
  discount: number;
  shippingCost: number;

  customer: Customer;
  orderlines: Orderline[];
}
