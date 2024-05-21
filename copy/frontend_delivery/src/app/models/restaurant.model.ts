import { Pizza } from "./pizza.model";

export interface Restaurant {
  id?: number;
  name: string;
  description: string;
  pizzaList?: Pizza[];
  photo?: string;  // URL to the photo
  file?: File;     // For file uploads
}
