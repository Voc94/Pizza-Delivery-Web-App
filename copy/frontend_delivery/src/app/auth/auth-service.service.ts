import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, switchMap, map } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../models/user.model';
import { ClientDTO } from '../models/client.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  public currentUser: Observable<User | null> = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private snackBar: MatSnackBar,
  ) {
    if (this.isBrowser()) {
      const storedUser: User | null = JSON.parse(sessionStorage.getItem('currentUser') || 'null');
      console.log('Stored User on Initialization:', storedUser);
      this.currentUserSubject.next(storedUser);
    }
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string): void {
    this.http.post<any>('http://localhost:8080/api/auth/authenticate', { email, password }, {
      headers: { 'Content-Type': 'application/json' }
    }).pipe(
      map(response => {
        console.log('Response from Authentication:', response);
        const parsedToken = this.parseJwt(response.token);
        const user: User = {
          token: response.token,
          username: response.username,
          id: parsedToken?.sub, // Assuming the user id is stored in the 'sub' field of the token
          email: response.email,
          role: response.role // Use role from the response directly
        };
        console.log('Parsed User Object:', user);
        if (this.isBrowser()) {
          sessionStorage.setItem('currentUser', JSON.stringify(user));
          sessionStorage.setItem('currentUserToken', response.token);
        }
        this.currentUserSubject.next(user);
        console.log('Current User Subject Value after login:', this.currentUserSubject.value);
        return user;
      }),
      catchError((error: HttpErrorResponse) => {
        this.snackBar.open(error.error.message || 'Login failed!', 'Close', { duration: 3000 });
        return of(null);
      })
    ).subscribe(user => {
      if (user) {
        this.router.navigate(['/home']);
      }
    });
  }

  private parseJwt(token: string): any {
    console.log('Token before parsing:', token);
    try {
      const parsedToken = JSON.parse(atob(token.split('.')[1]));
      console.log('Parsed Token:', parsedToken);
      return parsedToken;
    } catch (e) {
      console.error('Error parsing token:', e);
      return null;
    }
  }

  public getToken(): string | null {
    const token = sessionStorage.getItem('currentUserToken');
    console.log('Retrieved Token from sessionStorage:', token);
    return token;
  }

  logout(): void {
    const token = sessionStorage.getItem('currentUserToken');
    console.log('Token before logout:', token);
    if (token && this.isBrowser()) {
      this.http.delete('http://localhost:8080/api/auth/logout', {
        headers: { 'Authorization': `Bearer ${token}` }
      }).subscribe({
        next: () => {
          sessionStorage.removeItem('currentUser');
          sessionStorage.removeItem('currentUserToken');
          this.currentUserSubject.next(null);
          console.log('Current User Subject Value after logout:', this.currentUserSubject.value);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Logout error:', error);
          sessionStorage.removeItem('currentUser');
          sessionStorage.removeItem('currentUserToken');
          this.currentUserSubject.next(null);
          console.log('Current User Subject Value after logout error:', this.currentUserSubject.value);
          this.router.navigate(['/login']);
        }
      });
    } else {
      sessionStorage.removeItem('currentUser');
      sessionStorage.removeItem('currentUserToken');
      this.currentUserSubject.next(null);
      console.log('Current User Subject Value after logout (no token):', this.currentUserSubject.value);
      this.router.navigate(['/login']);
    }
  }

  private isBrowser(): boolean {
    return this.platformId === 'browser';
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/auth/register', { username, email, password }, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  getUserProfile(): Observable<ClientDTO> {
    const token = this.getToken();
    if (token) {
      const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
      return this.http.get<ClientDTO>('http://localhost:8080/api/user/profile', { headers });
    }
    throw new Error('User is not authenticated');
  }

  getUserId(): Observable<number> {
    const token = this.getToken();
    if (token) {
      const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
      return this.http.get<number>('http://localhost:8080/api/user/email', { headers });
    }
    throw new Error('User is not authenticated');
  }
}
