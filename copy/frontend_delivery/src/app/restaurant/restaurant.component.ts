import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RestaurantService } from './restaurantservice.service';
import { Restaurant } from '../models/restaurant.model';
import { MatTableDataSource } from '@angular/material/table';
import { AddRestaurantDialogComponent } from '../pages/add-restaurant-dialog/add-restaurant-dialog.component';
import { Pizza } from '../models/pizza.model';

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss']
})
export class RestaurantsComponent implements OnInit {
  dataSource = new MatTableDataSource<Restaurant>();
  displayedColumns: string[] = ['name', 'description', 'pizzas'];
  selectedRestaurant: Restaurant | null = null;
  searchTerm: string = '';

  constructor(private restaurantService: RestaurantService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantService.getRestaurants().subscribe({
      next: (data) => {
        console.log(data);  // Log the data here to check the structure
        this.dataSource.data = data;
      },
      error: (error) => console.error('Failed to load restaurants:', error)
    });
  }
  

  selectRestaurant(restaurant: Restaurant): void {
    this.selectedRestaurant = {...restaurant};
  }

  updateRestaurant(): void {
    if (this.selectedRestaurant) {
      this.restaurantService.updateRestaurant(this.selectedRestaurant.id, this.selectedRestaurant).subscribe({
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
      data: { name: '', description: '', file: null }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Assuming the file is also being returned in the result
        const file = result.file ? result.file : null;
        this.restaurantService.createRestaurant(result, file).subscribe({
          next: () => this.loadRestaurants(),
          error: (error) => console.error('Creation failed:', error)
        });
      }
    });
  }
  
  searchRestaurants(): void {
    this.restaurantService.searchRestaurants(this.searchTerm).subscribe({
        next: (data) => {
            this.dataSource.data = data; 
        },
        error: (error) => {
            console.error('Search failed:', error);
        }
    });
}
getPizzaNames(pizzas: Pizza[] | undefined): string {
  console.log("Pizzas:", pizzas);  // This will show what is being received by the function
  if (pizzas && pizzas.length > 0) {
    return pizzas.map(pizza => pizza.name).join(', ');
  } else {
    return 'No Pizzas';
  }
}




}
