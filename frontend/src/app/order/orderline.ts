import {Product} from '../product/product';
import {Ingredient} from '../ingredient/ingredient';

export class Orderline {
  id: number;
  qty: number;
  orderId: number;
  productId: number;
  purchasePrice: number;

  product: Product;
  ingredients: Ingredient[];
}
