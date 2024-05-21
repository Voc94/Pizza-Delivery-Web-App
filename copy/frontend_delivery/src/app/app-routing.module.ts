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

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },
  { path: 'logout', component: LogoutComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'messaging', component: MessagingComponent },
  { path: 'restaurant', component: RestaurantsComponent },
  { path: 'admin', component: AdminComponent, canActivate: [RoleGuardService], data: { expectedRole: 'ROLE_ADMIN' } },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }