import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { DIALOG_OPTIONS } from 'src/app/utils/dialog';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { DeleteComponent } from '../../common/delete/delete.component';
import { UserComponent } from '../user/user.component';
import { Page } from 'src/app/models/page';
import { DeleteData } from 'src/app/models/delete-data';
import { EMPTY_PAGE, SLUZBENIK } from 'src/app/utils/constants';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  constructor(
    private userService: UserService,
    private dialog: MatDialog
  ) { }

  private search = '';
  page: Page<User> = EMPTY_PAGE;
  pending = true;
  columns: string[] = ['role', 'email', 'firstName', 'lastName', 'actions'];

  get dataSource(): MatTableDataSource<User>{
    return new MatTableDataSource(this.page.content);
  }

  isSluzbenik(user: User): boolean{
    return user.role === SLUZBENIK;
  }

  edit(user: User): void{
    this.dialog.open(UserComponent, {...DIALOG_OPTIONS, ...{data: user}});
  }

  delete(id: number): void{
    const deleteData: DeleteData = {
      deleteFunction: () => this.userService.delete(id),
      refreshFunction: () => this.userService.announceRefreshData()
    };
    this.dialog.open(DeleteComponent, {...DIALOG_OPTIONS, ...{data: deleteData}});
  }

  fetchUsers(pageNumber: number): void{
    this.pending = true;
    // tslint:disable-next-line: deprecation
    this.userService.findAll(pageNumber, this.search).subscribe(
      (page: Page<User>) => {
        this.pending = false;
        this.page = page;
      }
    );
  }

  ngOnInit(): void {
    this.fetchUsers(0);
    // tslint:disable-next-line: deprecation
    this.userService.refreshData$.subscribe(() => this.fetchUsers(0));
    // tslint:disable-next-line: deprecation
    this.userService.searchData$.subscribe((search: string) => {
      this.search = search;
      this.fetchUsers(0);
    });
  }

}
