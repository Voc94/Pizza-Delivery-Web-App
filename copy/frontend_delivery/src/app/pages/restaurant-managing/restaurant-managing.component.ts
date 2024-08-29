import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Restaurant } from '../../models/restaurant.model';
import { AuthService } from '../../auth/auth-service.service';
import { Pizza } from '../../models/pizza.model';
import { RestaurantManagingService } from './restaurantmanagingservice.service';

@Component({
  selector: 'app-restaurant-managing',
  templateUrl: './restaurant-managing.component.html',
  styleUrls: ['./restaurant-managing.component.scss']
})
export class RestaurantManagingComponent implements OnInit {
  restaurantForm: FormGroup;
  pizzaForm: FormGroup;
  restaurant: Restaurant | null = null;
  managerId: number;

  constructor(
    private fb: FormBuilder,
    private restaurantManagingService: RestaurantManagingService,
    private authService: AuthService
  ) {
    this.managerId = this.authService.currentUserValue?.id || 0;

    this.restaurantForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
    });

    this.pizzaForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    this.loadRestaurant();
  }

  loadRestaurant(): void {
    this.restaurantManagingService.getManagedRestaurant().subscribe({
      next: (restaurant) => {
        this.restaurant = restaurant;
        this.restaurantForm.patchValue({
          name: restaurant.name,
          description: restaurant.description
        });
      },
      error: (error) => {
        console.error('Failed to load restaurant', error);
      }
    });
  }

  updateRestaurant(): void {
    if (this.restaurantForm.valid && this.restaurant) {
      const updatedRestaurant: Restaurant = {
        ...this.restaurant,
        ...this.restaurantForm.value
      };

      this.restaurantManagingService.updateManagedRestaurant(updatedRestaurant).subscribe({
        next: (restaurant) => {
          this.restaurant = restaurant;
          alert('Restaurant updated successfully');
        },
        error: (error) => {
          console.error('Failed to update restaurant', error);
        }
      });
    }
  }

  addPizza(): void {
    if (this.pizzaForm.valid && this.restaurant) {
      const newPizza: Pizza = {
        ...this.pizzaForm.value,
        restaurantId: this.restaurant.id || 0,
        id: 0 // Assuming id is auto-generated
      };

      this.restaurantManagingService.addPizza(newPizza).subscribe({
        next: (pizza) => {
          this.restaurant?.pizzaList?.push(pizza);
          this.pizzaForm.reset();
          alert('Pizza added successfully');
        },
        error: (error) => {
          console.error('Failed to add pizza', error);
        }
      });
    }
  }
}
