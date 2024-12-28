import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { environment } from 'src/environments/environment';
import { AccountsComponent } from './components/account/accounts/accounts.component';
import { AccountDetailsComponent } from './components/account/account-details/account-details.component';
import { ReportComponent } from './components/common/report/report.component';
import { BillsComponent } from './components/bill/bills/bills.component';
import { NotificationsComponent } from './components/account/notifications/notifications.component';
import { LoginComponent } from './components/user/login/login.component';
import { UsersComponent } from './components/user/users/users.component';
import { ADMIN, KLIJENT, SLUZBENIK } from './utils/constants';
import { AuthGuard } from './utils/auth.guard';

const routes: Routes = [
  {
    path: environment.loginRoute,
    component: LoginComponent
  },
  {
    path: environment.usersRoute,
    component: UsersComponent,
    canActivate: [AuthGuard],
    data: {roles: [ADMIN]}
  },
  {
    path: environment.accountsRoute,
    component: AccountsComponent,
    canActivate: [AuthGuard],
    data: {roles: [SLUZBENIK]}
  },
  {
    path: environment.accountRoute,
    component: AccountDetailsComponent,
    canActivate: [AuthGuard],
    data: {roles: [KLIJENT]}
  },
  {
    path: `${environment.billsRoute}/:type`,
    component: BillsComponent,
    canActivate: [AuthGuard],
    data: {roles: [KLIJENT]}
  },
  {
    path: environment.notificationsRoute,
    component: NotificationsComponent,
    canActivate: [AuthGuard],
    data: {roles: [ADMIN, KLIJENT]}
  },
  {
    path: environment.reportRoute,
    component: ReportComponent,
    canActivate: [AuthGuard],
    data: {roles: [SLUZBENIK]}
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: environment.loginRoute
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
