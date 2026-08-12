import { DatePipe } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { ReportTableService } from './report-table.service';

@Component({
  imports: [DatePipe],
  selector: 'app-report-table',
  template: `
    <p>Live report feed (refreshing every {{ pollSeconds() }}s)</p>

    <table>
      <thead>
        <tr>
          <th>Time</th>
          <th>Source</th>
          <th>Type</th>
          <th>Status</th>
          <th>Priority</th>
        </tr>
      </thead>
      <tbody>
        @for (report of reportTableService.reports(); track report.timestamp + report.sourceId) {
          <tr [class.high-priority]="report.priority === 'HIGH'">
            <td>{{ report.timestamp | date: 'HH:mm:ss' }}</td>
            <td>{{ report.sourceId }}</td>
            <td>{{ report.entityType }}</td>
            <td>{{ report.status }}</td>
            <td>{{ report.priority }}</td>
          </tr>
        } @empty {
          <tr>
            <td colspan="5">No reports yet -- waiting for the fake source generator...</td>
          </tr>
        }
      </tbody>
    </table>
  `,
  styles: `
    table {
      border-collapse: collapse;
      width: 100%;
      max-width: 800px;
    }
    th,
    td {
      border: 1px solid #ccc;
      padding: 0.4rem 0.8rem;
      text-align: left;
    }
    th {
      background: #f0f0f0;
    }
    .high-priority {
      background: #ffe3e3;
      font-weight: bold;
    }
  `,
})
export class ReportTableComponent {
  protected readonly reportTableService = inject(ReportTableService);

  protected readonly pollSeconds = computed(() => this.reportTableService.pollIntervalMs() / 1000);
}
