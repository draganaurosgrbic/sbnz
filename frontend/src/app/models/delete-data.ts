import { Observable } from 'rxjs';

export interface DeleteData{
    deleteFunction: () => Observable<boolean>;
    refreshFunction: () => void;
}

