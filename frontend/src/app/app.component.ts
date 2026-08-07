import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1>Asset Monitor</h1>
    <p>Frontend skeleton is running. Live feed table comes in a later step.</p>
  `,
})
export class AppComponent {}
