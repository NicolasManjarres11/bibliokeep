import { Routes } from '@angular/router';
import { MainLayoutComponent } from './core/layout/main-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { LibraryComponent } from './features/library/library.component';
import { LoansComponent } from './features/loans/loans.component';
import { Login } from './features/login/login';
import { authGuard } from './core/guard/auth-guard';
import { adminGuard } from './core/guard/admin-guard';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivateChild: [authGuard],
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'library',
        component: LibraryComponent
      },
      {
        path: 'loans',
        component: LoansComponent,
        canActivate: [adminGuard]
      }
    ]
  }
];
