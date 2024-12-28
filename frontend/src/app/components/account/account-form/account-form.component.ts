import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/utils/dialog';
import { Account } from 'src/app/models/account';
import { AccountService } from 'src/app/services/account.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-account-form',
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.scss']
})
export class AccountFormComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public account: Account,
    private accountService: AccountService,
    private dialogRef: MatDialogRef<AccountFormComponent>,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) { }

  pending = false;
  accountForm = this.formBuilder.group({
    firstName: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    lastName: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    jmbg: ['', [Validators.required, Validators.pattern('[0-9]{13}')]],
    birthDate: ['', [Validators.required, this.birthDateValidator()]],
    address: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    city: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    zipCode: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    email: ['', [Validators.required, Validators.email]]
  });

  confirm(): void{
    if (this.accountForm.invalid){
      return;
    }
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.accountService.save({...this.account, ...this.accountForm.value}).subscribe(
      (account: Account) => {
        this.pending = false;
        if (account){
          this.accountService.announceRefreshData();
          this.snackBar.open('Account saved!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close();
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  private birthDateValidator(): ValidatorFn{
    return (control: AbstractControl): ValidationErrors => {
      let dateValid = true;
      if (control.value >= new Date()){
        dateValid = false;
      }
      return dateValid ? null : {dateError: true};
    };
  }

  ngOnInit(): void {
    this.accountForm.reset(this.account);
    this.accountForm.controls.birthDate.reset(new Date(this.account.birthDate));
  }

}
