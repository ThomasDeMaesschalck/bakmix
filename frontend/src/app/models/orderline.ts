import {Product} from './product';
import {Ingredient} from './ingredient';

export class Orderline {
  id: number;
  qty: number;
  orderId: number;
  productId: number;
  purchasePrice: number;

  product: Product;
  ingredients: Ingredient[];
}
