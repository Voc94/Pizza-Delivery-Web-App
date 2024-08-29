import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth-service.service';
import { Restaurant } from '../models/restaurant.model';
import { Pizza } from '../models/pizza.model';

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private baseUrl = 'http://localhost:8080/api/restaurants';

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    if (token) {
      return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    } else {
      return new HttpHeaders();
    }
  }

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  getRestaurant(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  createRestaurant(restaurant: Restaurant, file: File | null): Observable<Restaurant> {
    const formData: FormData = new FormData();
    formData.append('name', restaurant.name);
    formData.append('description', restaurant.description);
    formData.append('manager_id', restaurant.manager_id.toString());
    if (file) {
      formData.append('file', file, file.name);
    }

    return this.http.post<Restaurant>(this.baseUrl, formData, { headers: this.getHeaders() });
  }

  updateRestaurant(id: number | undefined, restaurant: Restaurant, file: File | null): Observable<Restaurant> {
    const formData: FormData = new FormData();
    formData.append('name', restaurant.name);
    formData.append('description', restaurant.description);
    formData.append('manager_id', restaurant.manager_id.toString());
    if (file) {
      formData.append('file', file, file.name);
    }

    return this.http.put<Restaurant>(`${this.baseUrl}/${id}`, formData, { headers: this.getHeaders() });
  }

  deleteRestaurant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  searchRestaurants(query: string): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(`${this.baseUrl}/search`, { params: { query }, headers: this.getHeaders() });
  }

  getPhotoUrl(id: number): string {
    return `${this.baseUrl}/${id}/photo`;
  }
   getManagedRestaurant(): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.baseUrl}/restaurant`, { headers: this.getHeaders() });
  }
  getRestaurantById(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.baseUrl}/${id}`);
  }
  addPizza(pizza: Pizza): Observable<Pizza> {
    return this.http.post<Pizza>(`${this.baseUrl}/${pizza.restaurantId}/pizzas`, pizza);
  }

}
