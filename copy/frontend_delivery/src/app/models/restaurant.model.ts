import { Pizza } from "./pizza.model";

export interface Restaurant {
  id?: number;
  name: string;
  description: string;
  pizzaList?: Pizza[];
  photo?: string;
  file?: File;
  manager_id: number;
}
