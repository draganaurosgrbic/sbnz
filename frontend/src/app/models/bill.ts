import { Renewal } from './renewal';
import { Transaction } from './transaction';

export interface Bill{
    id: number;
    status: string;
    type: string;
    startDate: Date;
    endDate: Date;
    base: number;
    interest: number;
    balance: number;
    transactions: Transaction[];
    renewals: Renewal[];
}
