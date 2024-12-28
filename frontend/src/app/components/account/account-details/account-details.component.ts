import { Component, OnInit } from '@angular/core';
import { Account } from 'src/app/models/account';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss']
})
export class AccountDetailsComponent implements OnInit {

  constructor(
    private accountService: AccountService
  ) { }

  pending = true;
  account: Account;

  private getAccount(): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.accountService.findOne().subscribe(
      (account: Account) => {
        this.pending = false;
        this.account = account;
      }
    );
  }

  ngOnInit(): void {
    this.getAccount();
    // tslint:disable-next-line: deprecation
    this.accountService.refreshData$.subscribe(() => this.getAccount());
  }

}
