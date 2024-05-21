import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../auth/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  username: string = ''; 
  errors: any = {};  // Store validation errors here

  constructor(private authService: AuthService, private snackBar: MatSnackBar,private router: Router) {}

  onSubmit(): void {
    this.authService.register(this.username, this.email, this.password).subscribe({
      next: (response) => {

        console.log('Registration successful', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        // Display error message
        this.handleError(error);
      }
    });
  }

  private handleError(error: any): void {
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      this.snackBar.open('An error occurred:', error.error.message, { duration: 3000 });
    } else {
      // Backend returned an unsuccessful response code
      // If errors are structured in a specific way, you can extract and set them
      console.log('Registration failed:', error);
      this.errors = error.error.errors || {};
      this.snackBar.open(error.error.message || 'Registration failed!', 'Close', { duration: 3000 });
    }
  }
}
