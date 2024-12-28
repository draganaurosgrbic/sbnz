import { Component, OnInit } from '@angular/core';
import { Account } from 'src/app/models/account';
import { AccountService } from 'src/app/services/account.service';
import { Page } from 'src/app/models/page';
import { EMPTY_PAGE } from 'src/app/utils/constants';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  constructor(
    private accountService: AccountService
  ) { }

  private search = '';
  page: Page<Account> = EMPTY_PAGE;
  pending = true;

  fetchAccounts(pageNumber: number): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.accountService.findAll(pageNumber, this.search).subscribe(
      (page: Page<Account>) => {
        this.pending = false;
        this.page = page;
      }
    );
  }

  private getReport(index: number): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.accountService.getReport(index).subscribe(
      (accounts: Account[]) => {
        this.pending = false;
        this.page = {...EMPTY_PAGE, ...{content: accounts}};
      }
    );
  }

  ngOnInit(): void {
    this.fetchAccounts(0);
    // tslint:disable-next-line: deprecation
    this.accountService.refreshData$.subscribe(() => this.fetchAccounts(0));
    // tslint:disable-next-line: deprecation
    this.accountService.searchData$.subscribe((search: string) => {
      this.search = search;
      this.fetchAccounts(0);
    });
    // tslint:disable-next-line: deprecation
    this.accountService.report$.subscribe((index: number) => this.getReport(index));
  }

}
