import { Component, OnInit } from '@angular/core';
import { Report } from 'src/app/models/report';
import { BillService } from 'src/app/services/bill.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  constructor(
    private billService: BillService
  ) { }

  pending = true;
  report: Report;

  ngOnInit(): void {
    // tslint:disable-next-line: deprecation
    this.billService.getReport().subscribe(
      (report: Report) => {
        this.pending = false;
        this.report = report;
      }
    );
  }

}
