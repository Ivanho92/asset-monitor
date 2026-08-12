import { computed, inject, Injectable, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { interval, switchMap } from 'rxjs';
import { ReportApiService } from './report-api.service';
import { Report } from './report.model';

@Injectable()
export class ReportTableService {
  private readonly reportApi = inject(ReportApiService);

  public readonly reports = signal<Report[]>([]);
  public readonly pollIntervalMs = signal(3000);

  private readonly reports$ = interval(this.pollIntervalMs()).pipe(
    switchMap(() => this.reportApi.getReports()),
  );

  constructor() {
    this.reports$.pipe(takeUntilDestroyed()).subscribe({
      next: (reports) => this.reports.set(reports),
      error: (err) => console.error('Failed to fetch reports', err),
    });
  }
}
