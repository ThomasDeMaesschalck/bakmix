import DateTimeFormat = Intl.DateTimeFormat;

export class Ingredient {
  id: number;
  uniqueCode: string;
  type: string;
  brand: string;
  purchaseDate: string;
  purchaseLocation: string;
  lotNumber: string;
  volume: string;
  available: boolean;
  ingredienttracingId: number;
  recalled: boolean;
  expiry: string;
  linked: boolean;
}
