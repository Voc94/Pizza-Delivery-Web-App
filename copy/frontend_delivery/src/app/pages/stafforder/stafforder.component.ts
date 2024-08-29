import { Component, OnInit } from '@angular/core';
import { Order } from '../../models/order.model';
import { AuthService } from '../../auth/auth-service.service';
import { OrderService } from '../order/order-service.service';
import { RestaurantManagingService } from '../restaurant-managing/restaurantmanagingservice.service';

@Component({
  selector: 'app-staff-order',
  templateUrl: './stafforder.component.html',
  styleUrls: ['./stafforder.component.scss']
})
export class StaffOrderComponent implements OnInit {
  orders: Order[] = [];
  restaurantId: number = 0;
  displayedColumns: string[] = ['restaurantName', 'pizzaName', 'orderStatus', 'actions'];

  constructor(
    private orderService: OrderService,
    private authService: AuthService,
    private restaurantManagingService: RestaurantManagingService
  ) {}

  ngOnInit(): void {
    this.fetchRestaurantIdAndLoadOrders();
  }

  fetchRestaurantIdAndLoadOrders(): void {
    const userId = this.authService.currentUserValue?.id;
    if (userId) {
      this.restaurantManagingService.getRestaurantIdByUserId(userId).subscribe({
        next: (restaurantId) => {
          this.restaurantId = restaurantId;
          this.loadOrders();
        },
        error: (error) => {
          console.error('Failed to fetch restaurant ID', error);
        }
      });
    }
  }

  loadOrders(): void {
    this.orderService.getOrdersByRestaurantId(this.restaurantId).subscribe({
      next: (orders) => {
        this.orders = orders;
      },
      error: (error) => {
        console.error('Failed to load orders', error);
      }
    });
  }

  updateOrderStatus(orderId: number, newStatus: string): void {
    this.orderService.updateOrderStatus(orderId, newStatus).subscribe({
      next: (order) => {
        const index = this.orders.findIndex(o => o.orderId === order.orderId);
        if (index !== -1) {
          this.orders[index] = order;
        }
      },
      error: (error) => {
        console.error('Failed to update order status', error);
      }
    });
  }
}
