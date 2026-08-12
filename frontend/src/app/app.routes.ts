import { Routes } from '@angular/router';
import { ReportApiService } from './report-api.service';
import { ReportTableComponent } from './report-table.component';
import { ReportTableService } from './report-table.service';

export const routes: Routes = [
  {
    path: '',
    providers: [ReportApiService, ReportTableService],
    component: ReportTableComponent,
  },
];
