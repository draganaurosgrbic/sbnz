import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DeleteData } from 'src/app/models/delete-data';
import { DELETE_SUCCESS, DELETE_ERROR, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS, SNACKBAR_ERROR_OPTIONS } from 'src/app/utils/dialog';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) private deleteData: DeleteData,
    private dialogRef: MatDialogRef<DeleteComponent>,
    private snackBar: MatSnackBar
  ) { }

  pending = false;

  confirm(): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.deleteData.deleteFunction().subscribe(
      (response: boolean) => {
        this.pending = false;
        if (response){
          this.deleteData.refreshFunction();
          this.snackBar.open(DELETE_SUCCESS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close();
        }
        else{
          this.snackBar.open(DELETE_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

}
