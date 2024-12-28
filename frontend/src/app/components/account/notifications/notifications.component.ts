import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/notification';
import { NotificationService } from 'src/app/services/notification.service';
import { Page } from 'src/app/models/page';
import { EMPTY_PAGE } from 'src/app/utils/constants';
import { DeleteData } from 'src/app/models/delete-data';
import { MatDialog } from '@angular/material/dialog';
import { DeleteComponent } from '../../common/delete/delete.component';
import { DIALOG_OPTIONS } from 'src/app/utils/dialog';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  constructor(
    private notificationService: NotificationService,
    private dialog: MatDialog
  ) { }

  pending = true;
  page: Page<Notification> = EMPTY_PAGE;

  fetchNotifications(pageNumber: number): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.notificationService.findAll(pageNumber).subscribe(
      (page: Page<Notification>) => {
        this.pending = false;
        this.page = page;
      }
    );
  }

  delete(id: number): void{
    const deleteData: DeleteData = {
      deleteFunction: () => this.notificationService.delete(id),
      refreshFunction: () => this.notificationService.announceRefreshData()
    };
    this.dialog.open(DeleteComponent, {...DIALOG_OPTIONS, ...{data: deleteData}});
  }

  ngOnInit(): void {
    this.fetchNotifications(0);
    // tslint:disable-next-line: deprecation
    this.notificationService.refreshData$.subscribe(() => this.fetchNotifications(0));
  }
}
