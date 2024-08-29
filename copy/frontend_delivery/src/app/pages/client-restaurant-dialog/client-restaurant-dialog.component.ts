import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Restaurant } from '../../models/restaurant.model';
import { Pizza } from '../../models/pizza.model';
import { AuthService } from '../../auth/auth-service.service';
import { OrderService } from '../order/order-service.service';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-restaurant-dialog',
  templateUrl: './client-restaurant-dialog.component.html',
  styleUrls: ['./client-restaurant-dialog.component.scss']
})
export class RestaurantDialogComponent implements OnInit {
  userId: number | null = null;

  constructor(
    public dialogRef: MatDialogRef<RestaurantDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Restaurant,
    private orderService: OrderService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.getUserId().subscribe({
      next: (id) => {
        this.userId = id;
      },
      error: (error) => {
        console.error('Failed to fetch user ID:', error);
      }
    });
  }

  placeOrder(pizza: Pizza): void {
    if (this.userId == null) {
      console.error('User not authenticated');
      return;
    }

    if (this.data.id == null) {
      console.error('Restaurant ID is undefined');
      return;
    }

    const order: Order = {
      userId: this.userId,
      restaurantId: this.data.id,
      pizzaId: pizza.id,
      orderStatus: 'PLACED',
      orderId: 0,  // This will be set by the backend
      restaurantName: '',  // This will be set by the backend
      pizzaName: ''  // This will be set by the backend
    };

    this.orderService.placeOrder(order).subscribe({
      next: (order) => {
        console.log('Order placed:', order);
        this.dialogRef.close();
      },
      error: (error) => {
        console.error('Failed to place order:', error);
        console.error('Error details:', error.message);
      }
    });
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
