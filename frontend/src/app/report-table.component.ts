import { DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Table } from 'primeng/table';
import { ReportTableService } from './report-table.service';
import { Report } from './report.model';
import { ExclamationCircle } from '@primeicons/angular/exclamation-circle';

@Component({
  imports: [DatePipe, Table, ExclamationCircle],
  selector: 'app-report-table',
  template: `
    <h2>Live report feed</h2>

    <p-table
      [value]="reportTableService.reports()"
      [tableStyle]="{ 'min-width': '50rem' }"
      showGridlines
      stripedRows
      [paginator]="true"
      [rows]="10"
      [rowsPerPageOptions]="[10, 20, 50, 100]"
      [totalRecords]="reportTableService.reports().length"
    >
      <ng-template #header>
        <tr>
          <th [style.width]="0"></th>
          <th>Time</th>
          <th>Source</th>
          <th>Type</th>
          <th>Status</th>
          <th>Priority</th>
        </tr>
      </ng-template>
      <ng-template #body let-report let-rowIndex="rowIndex">
        <tr
          [class.high-priority]="isHighPriorityReport(report)"
          [animate.enter]="rowIndex === 0 ? 'row-enter' : ''"
        >
          <td>
            @if (isHighPriorityReport(report)) {
              <svg data-p-icon="exclamation-circle"></svg>
            }
          </td>
          <td>{{ report.timestamp | date: 'HH:mm:ss' }}</td>
          <td>{{ report.sourceId }}</td>
          <td>{{ report.entityType }}</td>
          <td>{{ report.status }}</td>
          <td>{{ report.priority }}</td>
        </tr>
      </ng-template>
      <ng-template #emptymessage>
        <tr>
          <td colspan="6">No reports found.</td>
        </tr>
      </ng-template>
    </p-table>
  `,
  styles: `
    .high-priority {
      background: var(--p-orange-50) !important;
    }
  `,
})
export class ReportTableComponent {
  protected readonly reportTableService = inject(ReportTableService);

  protected readonly isHighPriorityReport = (report: Report) => report.priority === 'HIGH';
}
