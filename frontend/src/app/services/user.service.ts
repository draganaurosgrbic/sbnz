import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { Login } from 'src/app/models/login';
import { User } from 'src/app/models/user';
import { catchError, map } from 'rxjs/operators';
import { EMPTY_PAGE, PAGE_SIZE } from 'src/app/utils/constants';
import { PasswordChange } from 'src/app/models/password-change';
import { Page } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly AUTH_PATH = 'auth';
  private readonly API_PATH = 'api/users';

  private refreshData: Subject<null> = new Subject();
  refreshData$: Observable<null> = this.refreshData.asObservable();

  private searchData: Subject<string> = new Subject();
  searchData$: Observable<string> = this.searchData.asObservable();

  findAll(page: number, search: string): Observable<Page<User>>{
    const params = new HttpParams().set('page', page + '').set('size', PAGE_SIZE + '').set('search', search);
    return this.http.get<Page<User>>(this.API_PATH, {params}).pipe(
      catchError(() => of(EMPTY_PAGE))
    );
  }

  save(user: User): Observable<User>{
    if (user.id){
      return this.http.put<User>(`${this.API_PATH}/${user.id}`, user).pipe(
        catchError(() => of(null))
      );
    }
    return this.http.post<User>(this.API_PATH, user).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_PATH}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  login(login: Login): Observable<User>{
    return this.http.post<User>(`${this.AUTH_PATH}/login`, login).pipe(
      catchError(() => of(null))
    );
  }

  changePassword(passwordChange: PasswordChange): Observable<boolean>{
    return this.http.put<null>(`${this.API_PATH}/change-password`, passwordChange).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

  announceSearchData(search: string): void{
    this.searchData.next(search);
  }

}
