import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';

// Import Angular Material modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list'; 
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AuthService } from './auth/auth-service.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { MessagingComponent } from './pages/messaging/messaging.component';
import { RestaurantsComponent } from './restaurant/restaurant.component';
import { AddRestaurantDialogComponent } from './pages/add-restaurant-dialog/add-restaurant-dialog.component';
import { ClientComponent } from './pages/client/client.component';
import { UserProfileDialogComponent } from './pages/user-profile-dialog/user-profile-dialog.component';
import { RestaurantDialogComponent } from './pages/client-restaurant-dialog/client-restaurant-dialog.component';
import { OrdersComponent } from './pages/order/orders.component';
import { RestaurantManagingComponent } from './pages/restaurant-managing/restaurant-managing.component';
import { EmployeeSistemComponent } from './pages/employee-sistem/employee-sistem.component';
import { StaffOrderComponent } from './pages/stafforder/stafforder.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LogoutComponent,
    HomeComponent,
    RegisterComponent,
    AdminComponent,
    MessagingComponent,
    RestaurantsComponent,
    AddRestaurantDialogComponent,
    ClientComponent,
    UserProfileDialogComponent,
    RestaurantDialogComponent,
    OrdersComponent,
    RestaurantManagingComponent,
    EmployeeSistemComponent,
    StaffOrderComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    ReactiveFormsModule,
    
    MatInputModule,
    MatIconModule,
    MatListModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatGridListModule,
    MatSelectModule,
    MatFormFieldModule,
    MatSnackBarModule,
    MatTableModule,
    FormsModule,
    MatDialogModule
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
