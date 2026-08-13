import { inject, Service, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ReportApiService } from './report-api.service';
import { ReportSocketService } from './report-socket.service';
import { Report } from './report.model';

@Service({ autoProvided: false })
export class ReportTableService {
  private readonly reportApi = inject(ReportApiService);
  private readonly reportSocket = inject(ReportSocketService);

  public readonly reports = signal<Report[]>([]);

  constructor() {
    this.reportApi
      .getReports()
      .pipe(takeUntilDestroyed())
      .subscribe({
        next: (reports) => this.reports.set(reports),
        error: (err) => console.error('Failed to load initial reports', err),
      });

    this.reportSocket.reports$.pipe(takeUntilDestroyed()).subscribe((report) => {
      const MAX_REPORTS = 50;
      this.reports.update((current) => [report, ...current].slice(0, MAX_REPORTS));
    });
  }
}
