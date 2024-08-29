import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../models/order.model';
import { AuthService } from '../../auth/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private orderUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : new HttpHeaders();
  }

  getOrdersByUserId(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.orderUrl}/user/${userId}`, { headers: this.getHeaders() });
  }

  getOrdersByRestaurantId(restaurantId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.orderUrl}/restaurant/${restaurantId}`, { headers: this.getHeaders() });
  }

  cancelOrder(orderId: number): Observable<Order> {
    return this.http.put<Order>(`${this.orderUrl}/${orderId}/status?status=CANCELED`, {}, { headers: this.getHeaders() });
  }

  placeOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(this.orderUrl, order, { headers: this.getHeaders() });
  }

  updateOrderStatus(orderId: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${this.orderUrl}/${orderId}/status?status=${status}`, {}, { headers: this.getHeaders() });
  }
}
