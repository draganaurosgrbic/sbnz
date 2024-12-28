import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Page } from 'src/app/models/page';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent {

  @Input() pagination!: Page<any>;
  @Input() pending: boolean;
  @Input() small: boolean;
  @Output() changedPage: EventEmitter<number> = new EventEmitter();

}
