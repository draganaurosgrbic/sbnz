import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatDrawer } from '@angular/material/sidenav';
import { DIALOG_OPTIONS } from 'src/app/utils/dialog';
import { Bill } from 'src/app/models/bill';
import { DrawerService } from 'src/app/services/drawer.service';
import { RenewalComponent } from '../renewal/renewal.component';
import { CloseComponent } from '../close/close.component';
import { TransactionComponent } from '../transaction/transaction.component';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.scss']
})
export class BillComponent implements AfterViewInit {

  constructor(
    public drawerService: DrawerService,
    private dialog: MatDialog
  ) { }

  @ViewChild('drawer') drawer: MatDrawer;
  @Input() bill!: Bill;

  transction(): void{
    this.dialog.open(TransactionComponent, {...DIALOG_OPTIONS, ...{data: this.bill.id}});
  }

  renewal(): void{
    this.dialog.open(RenewalComponent, {...DIALOG_OPTIONS, ...{data: this.bill.id}});
  }

  close(): void{
    this.dialog.open(CloseComponent, {...DIALOG_OPTIONS, ...{data: this.bill.id}});
  }

  ngAfterViewInit(): void{
    this.drawerService.register(this.drawer);
  }

}
