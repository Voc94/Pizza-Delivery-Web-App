import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from './auth/auth-service.service';
import { User } from './models/user.model';
import { UserProfileDialogComponent } from './pages/user-profile-dialog/user-profile-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  currentUser: User | null = null;

  constructor(
    private authService: AuthService,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.authService.currentUser.subscribe({
      next: (user) => {
        this.currentUser = user;
        console.log('Current User:', this.currentUser);
      },
      error: (error) => {
        console.error('Failed to get current user:', error);
      }
    });
  }

  showUserProfile(): void {
    if (this.currentUser) {
      this.authService.getUserProfile().subscribe({
        next: (profile) => {
          this.dialog.open(UserProfileDialogComponent, {
            width: '300px',
            data: profile
          });
        },
        error: (error) => {
          console.error('Failed to load user profile:', error);
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
