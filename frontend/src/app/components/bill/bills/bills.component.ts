import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Bill } from 'src/app/models/bill';
import { BillService } from 'src/app/services/bill.service';
import { Page } from 'src/app/models/page';
import { EMPTY_PAGE } from 'src/app/utils/constants';

@Component({
  selector: 'app-bills',
  templateUrl: './bills.component.html',
  styleUrls: ['./bills.component.scss']
})
export class BillsComponent implements OnInit {

  constructor(
    private billService: BillService,
    private route: ActivatedRoute
  ) { }

  private search = '';
  page: Page<Bill> = EMPTY_PAGE;
  pending = true;

  fetchBills(pageNumber: number): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.billService.findAll(this.route.snapshot.params.type, pageNumber, this.search).subscribe(
      (page: Page<Bill>) => {
        this.pending = false;
        this.page = page;
      }
    );
  }

  ngOnInit(): void {
    this.fetchBills(0);
    // tslint:disable-next-line: deprecation
    this.billService.refreshData$.subscribe(() => this.fetchBills(0));
    // tslint:disable-next-line: deprecation
    this.billService.searchData$.subscribe((search: string) => {
      this.search = search;
      this.fetchBills(0);
    });
  }

}
