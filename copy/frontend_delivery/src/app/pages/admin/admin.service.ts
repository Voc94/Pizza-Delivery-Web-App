import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from '../../models/user-dto.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl = 'http://localhost:8080/api/admin';  // Base URL for the API

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    // Retrieve the token from storage
    const token = localStorage.getItem('currentUserToken');

    // Optionally, handle cases where the token is not available
    if (!token) {
      console.error('No token found!');  // Log an error or handle gracefully
      return new HttpHeaders();  // Return empty headers or handle otherwise
    }

    // If the token exists, append it to the Authorization header
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.baseUrl}/users`, { headers: this.getHeaders() });
  }

  createUser(userDTO: UserDTO): Observable<UserDTO> {
    console.log('Creating user with:', userDTO); 
    return this.http.post<UserDTO>(`${this.baseUrl}/users`, userDTO, { headers: this.getHeaders() });
  }

  updateUser(userId: number, userDTO: UserDTO): Observable<UserDTO> {
    console.log('Updating user with:', userDTO); 
    return this.http.put<UserDTO>(`${this.baseUrl}/users/${userId}`, userDTO, { headers: this.getHeaders() });
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/users/${userId}`, { headers: this.getHeaders() });
  }

  getUsersCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/users/count`, { headers: this.getHeaders() });
  }

  exportUsersAsXml(): Observable<string> {
    return this.http.get(`${this.baseUrl}/users/xml`, { headers: this.getHeaders(), responseType: 'text' });
  }
}
