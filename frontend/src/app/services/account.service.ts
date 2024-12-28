import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { EMPTY_PAGE, PAGE_SIZE } from 'src/app/utils/constants';
import { Account } from 'src/app/models/account';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly API_PATH = 'api/accounts';

  private refreshData: Subject<null> = new Subject();
  refreshData$: Observable<null> = this.refreshData.asObservable();

  private searchData: Subject<string> = new Subject();
  searchData$: Observable<string> = this.searchData.asObservable();

  private report: Subject<number> = new Subject();
  report$: Observable<number> = this.report.asObservable();

  findAll(page: number, search: string): Observable<Page<Account>>{
    const params = new HttpParams().set('page', page + '').set('size', PAGE_SIZE + '').set('search', search);
    return this.http.get<Page<Account>>(this.API_PATH, {params}).pipe(
      catchError(() => of(EMPTY_PAGE))
    );
  }

  findOne(): Observable<Account>{
    return this.http.get<Account>(`${this.API_PATH}/my`).pipe(
      catchError(() => of(null))
    );
  }

  save(account: Account): Observable<Account>{
    if (account.id){
      return this.http.put<Account>(`${this.API_PATH}/${account.id}`, account).pipe(
        catchError(() => of(null))
      );
    }
    return this.http.post<Account>(this.API_PATH, account).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_PATH}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  getReport(index: number): Observable<Account[]>{
    return this.http.get<Account[]>(`${this.API_PATH}/report-${index}`).pipe(
      catchError(() => of([]))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

  announceSearchData(search: string): void{
    this.searchData.next(search);
  }

  announceReport(index: number): void{
    this.report.next(index);
  }

}
