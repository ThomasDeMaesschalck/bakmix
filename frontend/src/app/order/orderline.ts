import {Product} from "../product/product";

export class Orderline {
  id: number;
  orderId: number;
  productId: number;
  purchasePrice: number;

  product: Product;
}
