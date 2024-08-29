import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuardService } from './auth/auth.guard';
import { RoleGuardService } from './guards/role.guard';
import { RegisterComponent } from './pages/register/register.component';
import { AdminComponent } from './pages/admin/admin.component';
import { MessagingComponent } from './pages/messaging/messaging.component';
import { RestaurantsComponent } from './restaurant/restaurant.component';
import { ClientComponent } from './pages/client/client.component';
import { OrdersComponent } from './pages/order/orders.component';
import { RestaurantManagingComponent } from './pages/restaurant-managing/restaurant-managing.component';
import { EmployeeSistemComponent } from './pages/employee-sistem/employee-sistem.component';
import { StaffOrderComponent } from './pages/stafforder/stafforder.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },
  { path: 'logout', component: LogoutComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'messaging', component: MessagingComponent },
  { path: 'restaurant', component: RestaurantsComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_ADMIN' } },
  { path: 'admin', component: AdminComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_ADMIN' } },
  { path: 'client', component: ClientComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_CLIENT' } },
  { path: 'manager', component: RestaurantManagingComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_RESTAURANT_MANAGER' } },
  { path: 'employee', component: EmployeeSistemComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_RESTAURANT_MANAGER' } },
  { path: 'staff-orders', component: StaffOrderComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_STAFF' } },
  { path: 'orders', component: OrdersComponent, canActivate: [AuthGuardService] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
