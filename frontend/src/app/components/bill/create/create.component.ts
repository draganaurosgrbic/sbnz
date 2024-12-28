import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/utils/dialog';
import { BillResponse } from 'src/app/models/bill-response';
import { BillService } from 'src/app/services/bill.service';
import { MatDialogRef } from '@angular/material/dialog';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent {

  constructor(
    private accountService: AccountService,
    private billService: BillService,
    private dialogRef: MatDialogRef<CreateComponent>,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) { }

  billForm = this.formBuilder.group({
    type: ['', [Validators.required]],
    base: ['', [Validators.required]],
    months: ['', [Validators.required]]
  });

  pending = false;
  response: BillResponse;

  send(): void{
    if (this.billForm.invalid){
      return;
    }
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.billService.terms(this.billForm.value).subscribe(
      (response: BillResponse) => {
        this.pending = false;
        if (response){
          this.response = response;
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  accept(): void{
    this.pending = false;
    // tslint:disable-next-line: deprecation
    this.billService.create(this.billForm.value).subscribe(
      (response: BillResponse) => {
        this.pending = false;
        if (response){
          this.accountService.announceRefreshData();
          this.billService.announceRefreshData();
          this.snackBar.open('Bill saved!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close();
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

}
