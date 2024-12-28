import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { EMPTY_PAGE, PAGE_SIZE } from 'src/app/utils/constants';
import { Notification } from 'src/app/models/notification';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly API_PATH = 'api/notifications';

  private refreshData: Subject<null> = new Subject();
  refreshData$: Observable<null> = this.refreshData.asObservable();

  findAll(page: number): Observable<Page<Notification>>{
    const params = new HttpParams().set('page', page + '').set('size', PAGE_SIZE + '');
    return this.http.get<Page<Notification>>(this.API_PATH, {params}).pipe(
      catchError(() => of(EMPTY_PAGE))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_PATH}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

}
