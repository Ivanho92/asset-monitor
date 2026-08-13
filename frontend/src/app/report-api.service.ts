import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Report } from './report.model';

@Service({ autoProvided: false })
export class ReportApiService {
  private readonly http = inject(HttpClient);

  getReports(): Observable<Report[]> {
    return this.http.get<Report[]>(`${environment.apiUrl}/api/reports`);
  }
}
