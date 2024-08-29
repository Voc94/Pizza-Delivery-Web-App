import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../../models/user.model';
import { AuthService } from '../../auth/auth-service.service';
import { RestaurantManagingService } from '../restaurant-managing/restaurantmanagingservice.service';

@Component({
  selector: 'app-employee-system',
  templateUrl: './employee-sistem.component.html',
  styleUrls: ['./employee-sistem.component.scss']
})
export class EmployeeSistemComponent implements OnInit {
  employeeForm: FormGroup;
  employees: User[] = [];
  managerId: number;

  constructor(
    private fb: FormBuilder,
    private restaurantManagingService: RestaurantManagingService,
    private authService: AuthService
  ) {
    this.managerId = this.authService.currentUserValue?.id || 0;

    this.employeeForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.restaurantManagingService.getEmployees().subscribe({
      next: (employees) => {
        this.employees = employees;
      },
      error: (error) => {
        console.error('Failed to load employees', error);
      }
    });
  }

  addEmployee(): void {
    if (this.employeeForm.valid) {
      const newEmployee: User = this.employeeForm.value;

      this.restaurantManagingService.addEmployee(newEmployee).subscribe({
        next: (employee) => {
          this.employees.push(employee);
          this.employeeForm.reset();
          alert('Employee added successfully');
        },
        error: (error) => {
          console.error('Failed to add employee', error);
        }
      });
    }
  }
}
