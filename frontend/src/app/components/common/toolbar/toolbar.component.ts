import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { PasswordComponent } from 'src/app/components/user/password/password.component';
import { UserComponent } from 'src/app/components/user/user/user.component';
import { DIALOG_OPTIONS } from 'src/app/utils/dialog';
import { AccountService } from 'src/app/services/account.service';
import { BillService } from 'src/app/services/bill.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { environment } from 'src/environments/environment';
import { AccountFormComponent } from '../../account/account-form/account-form.component';
import { CreateComponent } from '../../bill/create/create.component';
import { ADMIN, KLIJENT, SLUZBENIK } from 'src/app/utils/constants';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {

  constructor(
    private storageService: StorageService,
    private userService: UserService,
    private accountService: AccountService,
    private billService: BillService,
    private router: Router,
    private dialog: MatDialog
  ) { }

  routes = environment;
  roles = {ADMIN, SLUZBENIK, KLIJENT};
  search: FormControl = new FormControl('');

  get role(): string{
    return this.storageService.getUser()?.role;
  }

  onRoute(param: string, includes?: boolean): boolean{
    if (includes){
      return this.router.url.substr(1).includes(param);
    }
    return this.router.url.substr(1) === param;
  }

  signOut(): void{
    this.storageService.removeUser();
    this.router.navigate([environment.loginRoute]);
  }

  openPasswordDialog(): void{
    this.dialog.open(PasswordComponent, DIALOG_OPTIONS);
  }

  openUserDialog(): void{
    this.dialog.open(UserComponent, {...DIALOG_OPTIONS, ...{data: {}}});
  }

  openAccountDialog(): void{
    this.dialog.open(AccountFormComponent, {...DIALOG_OPTIONS, ...{data: {}}});
  }

  openBillDialog(): void{
    this.dialog.open(CreateComponent, DIALOG_OPTIONS);
  }

  announceSearchData(): void{
    if (this.onRoute(this.routes.usersRoute)){
      this.userService.announceSearchData(this.search.value);
    }
    else if (this.onRoute(this.routes.accountsRoute)){
      this.accountService.announceSearchData(this.search.value);
    }
    else{
      this.billService.announceSearchData(this.search.value);
    }
  }

  announceReport(index: number): void{
    this.accountService.announceReport(index);
  }

}
