import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { MatSnackBar } from '@angular/material/snack-bar'; // For displaying notifications

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  public currentUser: Observable<User | null> = this.currentUserSubject.asObservable();  // Expose as observable

  constructor(
    private http: HttpClient, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private snackBar: MatSnackBar,
  ) {
    if (this.isBrowser()) {
      const storedUser: User | null = JSON.parse(sessionStorage.getItem('currentUser') || 'null');
      this.currentUserSubject.next(storedUser);
    }
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string): void {
    this.http.post<any>('http://localhost:8080/api/auth/authenticate', { email, password }, {
      headers: { 'Content-Type': 'application/json' }
    }).subscribe({
      next: (response) => {
        const user: User = {
          email: email,
          role: response.role,
          token: response.token,
          id: response.userId, // Ensure you're fetching the user ID from the response
          username: email
        };
        if (this.isBrowser()) {
          sessionStorage.setItem('currentUser', JSON.stringify(user));
          sessionStorage.setItem('currentUserToken', response.token);
        }
        this.currentUserSubject.next(user);
        this.router.navigate(['/home']);
      },
      error: (error: HttpErrorResponse) => {
        this.snackBar.open(error.error.message || 'Login failed!', 'Close', { duration: 3000 });
      }
    });
  }

  public getToken(): String|null{
   return sessionStorage.getItem('currentUserToken');
  }
  logout(): void {
    const token = sessionStorage.getItem('currentUserToken');
    if (token && this.isBrowser()) {
      this.http.delete('http://localhost:8080/api/auth/logout', {
        headers: { 'Authorization': `Bearer ${token}` }
      }).subscribe({
        next: () => {
          sessionStorage.removeItem('currentUser');
          sessionStorage.removeItem('currentUserToken');
          this.currentUserSubject.next(null);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Logout error:', error);
          sessionStorage.removeItem('currentUser');
          sessionStorage.removeItem('currentUserToken');
          this.currentUserSubject.next(null);
          this.router.navigate(['/login']);
        }
      });
    } else {
      sessionStorage.removeItem('currentUser');
      sessionStorage.removeItem('currentUserToken');
      this.currentUserSubject.next(null);
      this.router.navigate(['/login']);
    }
  }
  
  private isBrowser(): boolean {
      return this.platformId === 'browser';
  }

  // src/app/auth/auth-service.service.ts

register(username: string, email: string, password: string): Observable<any> {
  return this.http.post<any>('http://localhost:8080/api/auth/register', { username, email, password }, {
    headers: { 'Content-Type': 'application/json' }
  });
}
}
