import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/utils/dialog';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss']
})
export class PasswordComponent implements OnInit {

  constructor(
    private userService: UserService,
    private dialogRef: MatDialogRef<PasswordComponent>,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) { }

  pending = false;
  passwordForm = this.formBuilder.group({
    oldPassword: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    newPassword: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
    newPasswordConfirmation: ['', [this.passwordConfirmed()]]
  });

  confirm(): void{
    if (this.passwordForm.invalid){
      return;
    }
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.userService.changePassword(this.passwordForm.value).subscribe(
      (response: boolean) => {
        this.pending = false;
        if (response){
          this.snackBar.open('Password changed!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close();
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  private passwordConfirmed(): ValidatorFn{
    return (control: AbstractControl): ValidationErrors => {
      const passwordConfirmed: boolean = control.parent ?
      control.value === control.parent.get('newPassword').value : true;
      return passwordConfirmed ? null : {passwordError: true};
    };
  }

  ngOnInit(): void {
    // tslint:disable-next-line: deprecation
    this.passwordForm.controls.newPassword.valueChanges.subscribe(
      () => {
        this.passwordForm.controls.newPasswordConfirmation.updateValueAndValidity();
      }
    );
  }

}
