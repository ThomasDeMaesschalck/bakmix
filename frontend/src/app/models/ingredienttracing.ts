export class Ingredienttracing {
  constructor(ingredientId: number, orderlineId: number) {
    this.ingredientId = ingredientId;
    this.orderlineId = orderlineId;
  }

  id: number;
  orderlineId: number;
  ingredientId: number;
}
