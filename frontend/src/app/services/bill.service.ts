import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EMPTY_PAGE, PAGE_SIZE } from 'src/app/utils/constants';
import { Report } from 'src/app/models/report';
import { Bill } from 'src/app/models/bill';
import { BillRequest } from 'src/app/models/bill-request';
import { BillResponse } from 'src/app/models/bill-response';
import { RuleResponse } from 'src/app/models/rule-response';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly API_PATH = 'api/bills';

  private refreshData: Subject<null> = new Subject();
  refreshData$: Observable<null> = this.refreshData.asObservable();

  private searchData: Subject<string> = new Subject();
  searchData$: Observable<string> = this.searchData.asObservable();

  findAll(type: string, page: number, search: string): Observable<Page<Bill>>{
    const params = new HttpParams().set('rsd', (type === 'rsd') + '')
    .set('page', page + '').set('size', PAGE_SIZE + '').set('search', search);
    return this.http.get<Page<Bill>>(this.API_PATH, {params}).pipe(
      catchError(() => of(EMPTY_PAGE))
    );
  }

  create(response: BillRequest): Observable<BillResponse>{
    return this.http.post<Bill>(this.API_PATH, response).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<RuleResponse>{
    return this.http.delete<null>(`${this.API_PATH}/${id}`).pipe(
      catchError(() => of(null))
    );
  }

  terms(request: BillRequest): Observable<BillResponse>{
    return this.http.post<BillResponse>(`${this.API_PATH}/terms`, request).pipe(
      catchError(() => of(null))
    );
  }

  transaction(id: number, amount: number): Observable<RuleResponse>{
    return this.http.put(`${this.API_PATH}/${id}/increase/${amount}`, null).pipe(
      catchError(() => of(null))
    );
  }

  renewal(id: number, amount: number): Observable<RuleResponse>{
    return this.http.put(`${this.API_PATH}/${id}/renew/${amount}`, null).pipe(
      catchError(() => of(null))
    );
  }

  getReport(): Observable<Report>{
    return this.http.get<Report>(`${this.API_PATH}/report`).pipe(
      catchError(() => of(null))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

  announceSearchData(search: string): void{
    this.searchData.next(search);
  }

}
