import { ITissu } from 'app/entities/tissu/tissu.model';

export interface ISeller {
  id?: number;
  name?: string | null;
  webSite?: string | null;
  tissus?: ITissu[] | null;
}

export class Seller implements ISeller {
  constructor(public id?: number, public name?: string | null, public webSite?: string | null, public tissus?: ITissu[] | null) {}
}

export function getSellerIdentifier(seller: ISeller): number | undefined {
  return seller.id;
}
