import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ClientService } from './client-service.service';
import { Restaurant } from '../../models/restaurant.model';
import { RestaurantDialogComponent } from '../client-restaurant-dialog/client-restaurant-dialog.component';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit {
  dataSource = new MatTableDataSource<Restaurant>();
  searchTerm: string = '';

  constructor(private clientService: ClientService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.clientService.getRestaurants().subscribe({
      next: (data) => {
        data.forEach(restaurant => {
          if (restaurant.id) {
            restaurant.photo = this.clientService.getPhotoUrl(restaurant.id);
            console.log(`Restaurant: ${restaurant.name}, Photo URL: ${restaurant.photo}`);
          }
        });
        this.dataSource.data = data;
      },
      error: (error) => {
        console.error('Failed to load restaurants:', error);
        console.error('Error details:', error.message);
      }
    });
  }

  searchRestaurants(): void {
    if (this.searchTerm) {
      this.clientService.searchRestaurants(this.searchTerm).subscribe({
        next: (data) => {
          data.forEach(restaurant => {
            if (restaurant.id) {
              restaurant.photo = this.clientService.getPhotoUrl(restaurant.id);
              console.log(`Search Result - Restaurant: ${restaurant.name}, Photo URL: ${restaurant.photo}`);
            }
          });
          this.dataSource.data = data;
        },
        error: (error) => {
          console.error('Search failed:', error);
          console.error('Error details:', error.message);
        }
      });
    } else {
      this.loadRestaurants();
    }
  }

  openRestaurantDialog(restaurant: Restaurant): void {
    this.dialog.open(RestaurantDialogComponent, {
      width: '400px',
      data: restaurant
    });
  }
}
