import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../../models/user-dto.model';
import { AdminService } from './admin.service';
import { EMPTY, catchError } from 'rxjs';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  dataSource: UserDTO[] = [];
  displayedColumns: string[] = ['id', 'username', 'email', 'role', 'actions'];
  selectedUser: UserDTO | null = null;
  usersCount: number = 0;
  roles: string[] = ['USER', 'ADMIN', 'RESTAURANT_MANAGER', 'STAFF']; // Define roles

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
      this.loadUsers();
      this.fetchUsersCount();
      
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe(users => this.dataSource = users);
  }

  resetForm(): void {
    this.selectedUser = { id: 0, username: '', email: '', role: '',password: '' }; // Adjust according to your UserDTO interface
  }
  fetchUsersCount(): void {
    // Call the service method to fetch the count
    this.adminService.getUsersCount().pipe(
      catchError(error => {
        console.error('Error fetching users count:', error);
        return EMPTY;
      })
    ).subscribe({
      next: (count: number) => {
        this.usersCount = count; // Update the count
      }
    });
  }
  onSubmit(): void {
    if (this.selectedUser) {
      const userOperation = this.selectedUser.id ? 
        this.adminService.updateUser(this.selectedUser.id, this.selectedUser) :
        this.adminService.createUser(this.selectedUser);
      
      userOperation.subscribe({
        next: () => {
          this.loadUsers();
          this.selectedUser = null;
        },
        error: (error) => {
          console.error('Error saving user:', error);
        }
      });
    }
  }
  deleteUser(userId: number): void {
    this.adminService.deleteUser(userId).subscribe({
      next: () => {
        this.loadUsers();  // Refresh the list after deletion
        console.log('User deleted successfully');
      },
      error: (error) => {
        console.error('Error deleting user:', error);
      }
    });
  }
  
}
