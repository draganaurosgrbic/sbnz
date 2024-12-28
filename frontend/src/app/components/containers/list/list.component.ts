import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Page } from 'src/app/models/page';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  @Input() title: string;
  @Input() pending: boolean;
  @Input() length: number;
  @Input() pagination!: Page<any>;
  @Output() changedPage: EventEmitter<number> = new EventEmitter();

}
