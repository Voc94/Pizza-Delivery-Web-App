import { Component, OnInit } from '@angular/core';
import { Order } from '../../models/order.model';
import { AuthService } from '../../auth/auth-service.service';
import { OrderService } from './order-service.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  userId: number | null = null;
  displayedColumns: string[] = ['restaurantName', 'pizzaName', 'orderStatus', 'actions'];

  constructor(private orderService: OrderService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getUserId().subscribe({
      next: (id) => {
        this.userId = id;
        this.loadOrders();
      },
      error: (error) => {
        console.error('Failed to fetch user ID:', error);
      }
    });
  }

  loadOrders(): void {
    if (this.userId != null) {
      this.orderService.getOrdersByUserId(this.userId).subscribe({
        next: (orders) => {
          this.orders = orders;
        },
        error: (error) => {
          console.error('Failed to fetch orders:', error);
        }
      });
    }
  }

  cancelOrder(orderId: number): void {
    this.orderService.cancelOrder(orderId).subscribe({
      next: () => {
        this.loadOrders();
      },
      error: (error) => {
        console.error('Failed to cancel order:', error);
      }
    });
  }
}
