import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DialogComponent>
  ) { }

  @Input() title: string;
  @Input() warn: boolean;
  @Input() pending: boolean;
  @Input() disabled: boolean;
  @Output() confirmed: EventEmitter<null> = new EventEmitter();

}
