import { Injectable } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';

@Injectable({
  providedIn: 'root'
})
export class DrawerService {

  private drawers: MatDrawer[] = [];

  register(drawer: MatDrawer): void{
    this.drawers.push(drawer);
  }

  closeOthers(drawer: MatDrawer): void{
    for (const d of this.drawers){
      if (d !== drawer){
        d.close();
      }
    }
  }

}
