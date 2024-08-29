import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Restaurant } from '../../models/restaurant.model';
import { Pizza } from '../../models/pizza.model';
import { User } from '../../models/user.model';
import { Order } from '../../models/order.model';
import { AuthService } from '../../auth/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class RestaurantManagingService {
  private apiUrl = 'http://localhost:8080/api/manager';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : new HttpHeaders();
  }

  getManagedRestaurant(): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.apiUrl}/restaurant`, { headers: this.getHeaders() });
  }

  updateManagedRestaurant(restaurant: Restaurant): Observable<Restaurant> {
    return this.http.put<Restaurant>(`${this.apiUrl}/restaurant`, restaurant, { headers: this.getHeaders() });
  }

  addPizza(pizza: Pizza): Observable<Pizza> {
    return this.http.post<Pizza>(`${this.apiUrl}/restaurant/pizzas`, pizza, { headers: this.getHeaders() });
  }

  getEmployees(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/employees`, { headers: this.getHeaders() });
  }

  addEmployee(employee: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/employee`, employee, { headers: this.getHeaders() });
  }

  getOrdersByRestaurantId(restaurantId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/orders/restaurant/${restaurantId}`, { headers: this.getHeaders() });
  }

  updateOrderStatus(orderId: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/orders/${orderId}/status?status=${status}`, {}, { headers: this.getHeaders() });
  }

  getRestaurantIdByUserId(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/restaurant/user/${userId}`, { headers: this.getHeaders() });
  }
}
