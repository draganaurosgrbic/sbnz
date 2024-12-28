export interface Report{
    rsd: ReportUnit;
    eur: ReportUnit;
    usd: ReportUnit;
    chf: ReportUnit;
    gbp: ReportUnit;
}

interface ReportUnit{
    activeCnt: number;
    closedCnt: number;
    abortedShare: number;
    baseAvg: number;
    monthsAvg: number;
    increaseAvg: number;
    renewAvg: number;
}

