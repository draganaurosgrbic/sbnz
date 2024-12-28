import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private readonly USER_KEY = 'user';

  getUser(): User{
    return JSON.parse(localStorage.getItem(this.USER_KEY));
  }

  setUser(user: User): void{
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  removeUser(): void{
    localStorage.removeItem(this.USER_KEY);
  }

}
