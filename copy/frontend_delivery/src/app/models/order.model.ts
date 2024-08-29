export interface Order {
  orderId: number;
  userId: number;
  restaurantId: number;
  pizzaId: number;
  orderStatus: string;
  restaurantName?: string;
  pizzaName?: string;
}
