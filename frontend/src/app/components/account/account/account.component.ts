import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DIALOG_OPTIONS } from 'src/app/utils/dialog';
import { Account } from 'src/app/models/account';
import { AccountService } from 'src/app/services/account.service';
import { DeleteComponent } from '../../common/delete/delete.component';
import { DeleteData } from 'src/app/models/delete-data';
import { AccountFormComponent } from '../account-form/account-form.component';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent {

  constructor(
    private accountService: AccountService,
    private dialog: MatDialog
  ) { }

  @Input() account!: Account;
  @Input() myAccount: boolean;

  edit(): void{
    this.dialog.open(AccountFormComponent, {...DIALOG_OPTIONS, ...{data: this.account}});
  }

  delete(): void{
    const deleteData: DeleteData = {
      deleteFunction: () => this.accountService.delete(this.account.id),
      refreshFunction: () => this.accountService.announceRefreshData()
    };
    this.dialog.open(DeleteComponent, {...DIALOG_OPTIONS, ...{data: deleteData}});
  }

}
