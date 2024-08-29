import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Restaurant } from '../models/restaurant.model';
import { MatTableDataSource } from '@angular/material/table';
import { AddRestaurantDialogComponent } from '../pages/add-restaurant-dialog/add-restaurant-dialog.component';
import { Pizza } from '../models/pizza.model';
import { RestaurantService } from './restaurantservice.service';

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss']
})
export class RestaurantsComponent implements OnInit {
  dataSource = new MatTableDataSource<Restaurant>();
  displayedColumns: string[] = ['name', 'description', 'pizzas', 'photo'];
  selectedRestaurant: Restaurant | null = null;
  searchTerm: string = '';

  constructor(private restaurantService: RestaurantService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantService.getRestaurants().subscribe({
      next: (data) => {
        data.forEach(restaurant => {
          if (restaurant.id) {
            restaurant.photo = this.restaurantService.getPhotoUrl(restaurant.id);
          }
        });
        this.dataSource.data = data;
      },
      error: (error) => console.error('Failed to load restaurants:', error)
    });
  }

  selectRestaurant(restaurant: Restaurant): void {
    this.selectedRestaurant = { ...restaurant };
  }

  updateRestaurant(): void {
    if (this.selectedRestaurant && this.selectedRestaurant.id !== undefined) {
      const file: File | null = this.selectedRestaurant.file || null;
      this.restaurantService.updateRestaurant(this.selectedRestaurant.id, this.selectedRestaurant, file).subscribe({
        next: () => {
          this.loadRestaurants();
          this.selectedRestaurant = null;  // Clear selection after update
        },
        error: (error) => console.error('Update failed:', error)
      });
    }
  }

  openAddDialog(): void {
    const dialogRef = this.dialog.open(AddRestaurantDialogComponent, {
      width: '250px',
      data: { name: '', description: '', file: null, manager_id: null }  // Include manager_id
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const file: File | null = result.file || null;
        const newRestaurant = { name: result.name, description: result.description, manager_id: result.manager_id };
        this.restaurantService.createRestaurant(newRestaurant, file).subscribe({
          next: () => this.loadRestaurants(),
          error: (error) => console.error('Creation failed:', error)
        });
      }
    });
  }

  searchRestaurants(): void {
    if (this.searchTerm) {
      this.restaurantService.searchRestaurants(this.searchTerm).subscribe({
        next: (data) => {
          this.dataSource.data = data;
        },
        error: (error) => {
          console.error('Search failed:', error);
        }
      });
    } else {
      this.loadRestaurants();
    }
  }

  getPizzaNames(pizzas: Pizza[] | undefined): string {
    if (pizzas && pizzas.length > 0) {
      return pizzas.map(pizza => pizza.name).join(', ');
    } else {
      return 'No Pizzas';
    }
  }
}
