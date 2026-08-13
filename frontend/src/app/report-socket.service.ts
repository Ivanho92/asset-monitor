import { OnDestroy, Service } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import { environment } from '../environments/environment';
import { Report } from './report.model';

@Service({ autoProvided: false })
export class ReportSocketService implements OnDestroy {
  private readonly reportsSubject = new Subject<Report>();
  public readonly reports$ = this.reportsSubject.asObservable();

  private readonly client = new Client({
    brokerURL: environment.wsUrl,
    reconnectDelay: 5000,
  });

  constructor() {
    this.client.onConnect = () => {
      this.client.subscribe('/topic/reports', (message) => {
        const report: Report = JSON.parse(message.body);
        this.reportsSubject.next(report);
      });
    };

    this.client.onStompError = (frame) => {
      console.error('WebSocket STOMP error', frame);
    };

    this.client.activate();
  }

  ngOnDestroy(): void {
    void this.client.deactivate();
  }
}
