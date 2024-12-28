import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rule-response',
  templateUrl: './rule-response.component.html',
  styleUrls: ['./rule-response.component.scss']
})
export class RuleResponseComponent {

  @Input() small: boolean;

}
