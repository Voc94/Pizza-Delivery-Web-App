import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Restaurant } from '../../models/restaurant.model';
import { AuthService } from '../../auth/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private baseUrl = 'http://localhost:8080/api/client/restaurants';

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    if (token) {
      console.log(`Token found: ${token}`); // Log the token
      return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    } else {
      console.log('No token found');
      return new HttpHeaders();
    }
  }

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  getRestaurant(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  searchRestaurants(query: string): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(`${this.baseUrl}/search`, { params: { query }, headers: this.getHeaders() });
  }

  getPhotoUrl(id: number): string {
    return `${this.baseUrl}/${id}/photo`;
  }
}
